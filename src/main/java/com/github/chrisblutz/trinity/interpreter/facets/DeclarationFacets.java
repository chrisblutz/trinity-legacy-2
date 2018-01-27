package com.github.chrisblutz.trinity.interpreter.facets;

import com.github.chrisblutz.trinity.interpreter.*;
import com.github.chrisblutz.trinity.interpreter.actions.VariableProcedureAction;
import com.github.chrisblutz.trinity.interpreter.components.DeclarationExpression;
import com.github.chrisblutz.trinity.interpreter.instructions.*;
import com.github.chrisblutz.trinity.lang.TyMethod;
import com.github.chrisblutz.trinity.lang.errors.Errors;
import com.github.chrisblutz.trinity.lang.procedures.ProcedureAction;
import com.github.chrisblutz.trinity.parser.blocks.Block;
import com.github.chrisblutz.trinity.parser.tokens.SourceToken;
import com.github.chrisblutz.trinity.parser.tokens.Token;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;


/**
 * @author Christopher Lutz
 */
public class DeclarationFacets {
    
    public static void registerFacets() {
        
        DeclarationExpressions.registerDeclarationExpression(Token.MODULE, new DeclarationExpression() {
            
            @Override
            public InstructionSet interpret(SourceToken[] line, Block next, Interpreter interpreter, Location location) {
                
                if (line[0].getToken() == Token.TOKEN_STRING) {
                    
                    ProcedureAction nextAction = null;
                    if (next != null) {
                        
                        nextAction = interpreter.interpret(next, false);
                    }
                    
                    return new ModuleDeclarationInstructionSet(line[0].getContents(), nextAction, location);
                    
                } else {
                    
                    Errors.reportSyntaxError("Invalid token " + line[0].getToken().toString() + " in module declaration.  Expected " + Token.TOKEN_STRING.toString() + ".", interpreter.getSourceEntry(), location.getLineNumber(), line[0].getIndex());
                    return null;
                }
            }
        });
        DeclarationExpressions.registerDeclarationExpression(Token.CLASS, new DeclarationExpression() {
            
            @Override
            public InstructionSet interpret(SourceToken[] line, Block next, Interpreter interpreter, Location location) {
                
                String name;
                String superclass = null;
                List<String> interfaces = new ArrayList<>();
                boolean isFinal = false;
                
                int offset = 0;
                while (line[offset].getToken() != Token.TOKEN_STRING) {
                    
                    switch (line[offset++].getToken()) {
                        
                        case FINAL:
                            
                            isFinal = true;
                            break;
                        
                        default:
                            
                            Errors.reportSyntaxError("Invalid token " + line[offset].getToken().toString() + " in class declaration.", interpreter.getSourceEntry(), location.getLineNumber(), line[offset].getIndex());
                    }
                }
                
                name = line[offset++].getContents();
                
                if (offset < line.length && line[offset].getToken() == Token.LEFT_SHIFT_OP) {
                    
                    offset++;
                    StringBuilder str = new StringBuilder();
                    for (; offset < line.length; offset++) {
                        
                        SourceToken token = line[offset];
                        
                        if (token.getToken() == Token.RIGHT_SHIFT_OP) {
                            
                            break;
                            
                        } else if (token.getToken() == Token.TOKEN_STRING) {
                            
                            str.append(token.getContents());
                            
                        } else {
                            
                            str.append(token.getToken().getLiteral());
                        }
                    }
                    
                    superclass = str.toString();
                }
                
                if (offset < line.length && line[offset].getToken() == Token.RIGHT_SHIFT_OP) {
                    
                    offset++;
                    SourceToken[] names = new SourceToken[line.length - offset];
                    System.arraycopy(line, line.length - names.length, names, 0, names.length);
                    
                    List<List<SourceToken>> nameTokens = interpreter.splitTokens(names, Token.COMMA, -1);
                    for (List<SourceToken> tokens : nameTokens) {
                        
                        StringBuilder str = new StringBuilder();
                        for (SourceToken token : tokens) {
                            
                            if (token.getToken() == Token.TOKEN_STRING) {
                                
                                str.append(token.getContents());
                                
                            } else {
                                
                                str.append(token.getToken().getLiteral());
                            }
                        }
                        
                        interfaces.add(str.toString());
                    }
                }
                
                ProcedureAction nextAction = null;
                if (next != null) {
                    
                    nextAction = interpreter.interpret(next, false);
                }
                
                return new ClassDeclarationInstructionSet(name, superclass, interfaces.toArray(new String[interfaces.size()]), isFinal, nextAction, location);
            }
        });
        DeclarationExpressions.registerDeclarationExpression(Token.INTERFACE, new DeclarationExpression() {
            
            @Override
            public InstructionSet interpret(SourceToken[] line, Block next, Interpreter interpreter, Location location) {
                
                String name;
                List<String> interfaces = new ArrayList<>();
                
                int offset = 0;
                if (line[offset].getToken() != Token.TOKEN_STRING) {
                    
                    Errors.reportSyntaxError("Invalid token " + line[offset].getToken().toString() + " in class declaration.", interpreter.getSourceEntry(), location.getLineNumber(), line[offset].getIndex());
                }
                
                name = line[offset++].getContents();
                
                if (offset < line.length && line[offset].getToken() == Token.LEFT_SHIFT_OP) {
                    
                    offset++;
                    SourceToken[] names = new SourceToken[line.length - offset];
                    System.arraycopy(line, line.length - names.length, names, 0, names.length);
                    
                    List<List<SourceToken>> nameTokens = interpreter.splitTokens(names, Token.COMMA, -1);
                    for (List<SourceToken> tokens : nameTokens) {
                        
                        StringBuilder str = new StringBuilder();
                        for (SourceToken token : tokens) {
                            
                            if (token.getToken() == Token.TOKEN_STRING) {
                                
                                str.append(token.getContents());
                                
                            } else {
                                
                                str.append(token.getToken().getLiteral());
                            }
                        }
                        
                        interfaces.add(str.toString());
                    }
                }
                
                ProcedureAction nextAction = null;
                if (next != null) {
                    
                    nextAction = interpreter.interpret(next, false);
                }
                
                return new InterfaceDeclarationInstructionSet(name, interfaces.toArray(new String[interfaces.size()]), nextAction, location);
            }
        });
        
        DeclarationExpressions.registerDeclarationExpression(Token.DEF, new DeclarationExpression() {
            
            @Override
            public InstructionSet interpret(SourceToken[] line, Block next, Interpreter interpreter, Location location) {
                
                boolean isStatic = false, isSecure = false, isFinal = false;
                String name;
                
                int offset = 0;
                while (!leadsWithName(line, offset)) {
                    
                    switch (line[offset++].getToken()) {
                        
                        case STATIC:
                            
                            isStatic = true;
                            break;
                        
                        case SECURE:
                            
                            isSecure = true;
                            break;
                        
                        case FINAL:
                            
                            isFinal = true;
                            break;
                        
                        default:
                            
                            Errors.reportSyntaxError("Invalid token " + line[offset].getToken().toString() + " in method declaration.", interpreter.getSourceEntry(), location.getLineNumber(), line[offset].getIndex());
                    }
                }
                
                if (line[offset].getToken() == Token.TOKEN_STRING) {
                    
                    name = line[offset++].getContents();
                    
                } else if (Operators.isOperatorMethod(line[offset].getToken())) {
                    
                    name = line[offset++].getToken().getReadable();
                    
                } else if (offset + 2 < line.length && line[offset].getToken() == Token.LEFT_SQUARE_BRACKET && line[offset + 1].getToken() == Token.RIGHT_SQUARE_BRACKET && line[offset + 2].getToken() == Token.ASSIGNMENT_OP) {
                    
                    name = "[]=";
                    offset += 3;
                    
                } else if (offset + 1 < line.length && line[offset].getToken() == Token.LEFT_SQUARE_BRACKET && line[offset + 1].getToken() == Token.RIGHT_SQUARE_BRACKET) {
                    
                    name = "[]";
                    offset += 2;
                    
                } else {
                    
                    Errors.reportSyntaxError("Invalid token " + line[offset].getToken().toString() + " in module declaration.", interpreter.getSourceEntry(), location.getLineNumber(), line[offset].getIndex());
                    name = "";
                }
                
                Parameters parameters = new Parameters(new ArrayList<>(), new LinkedHashMap<>(), null, null);
                
                if (offset + 1 < line.length && line[offset].getToken() == Token.LEFT_PARENTHESIS && line[line.length - 1].getToken() == Token.RIGHT_PARENTHESIS) {
                    
                    SourceToken[] paramTokens = new SourceToken[line.length - offset - 2];
                    System.arraycopy(line, offset + 1, paramTokens, 0, paramTokens.length);
                    parameters = interpreter.interpretParameters(paramTokens, location);
                }
                
                ProcedureAction nextAction;
                if (next == null) {
                    
                    nextAction = TyMethod.DEFAULT_METHOD;
                    
                } else {
                    
                    nextAction = interpreter.interpret(next, true);
                }
                
                return new MethodDeclarationInstructionSet(name, isStatic, isSecure, isFinal, parameters, nextAction, location);
            }
            
            private boolean leadsWithName(SourceToken[] line, int offset) {
                
                if (line[offset].getToken() == Token.TOKEN_STRING) {
                    
                    return true;
                    
                } else if (Operators.isOperatorMethod(line[offset].getToken())) {
                    
                    return true;
                    
                } else if (offset + 2 < line.length && line[offset].getToken() == Token.LEFT_SQUARE_BRACKET && line[offset + 1].getToken() == Token.RIGHT_SQUARE_BRACKET && line[offset + 2].getToken() == Token.ASSIGNMENT_OP) {
                    
                    return true;
                    
                } else if (offset + 1 < line.length && line[offset].getToken() == Token.LEFT_SQUARE_BRACKET && line[offset + 1].getToken() == Token.RIGHT_SQUARE_BRACKET) {
                    
                    return true;
                }
                
                return false;
            }
        });
        DeclarationExpressions.registerDeclarationExpression(Token.DEF_NATIVE, new DeclarationExpression() {
            
            @Override
            public InstructionSet interpret(SourceToken[] line, Block next, Interpreter interpreter, Location location) {
                
                boolean isStatic = false, isSecure = false, isFinal = false;
                String name;
                
                int offset = 0;
                while (!leadsWithName(line, offset)) {
                    
                    switch (line[offset++].getToken()) {
                        
                        case STATIC:
                            
                            isStatic = true;
                            break;
                        
                        case SECURE:
                            
                            isSecure = true;
                            break;
                        
                        case FINAL:
                            
                            isFinal = true;
                            break;
                        
                        default:
                            
                            Errors.reportSyntaxError("Invalid token " + line[offset].getToken().toString() + " in method declaration.", interpreter.getSourceEntry(), location.getLineNumber(), line[offset].getIndex());
                    }
                }
                
                if (line[offset].getToken() == Token.TOKEN_STRING) {
                    
                    name = line[offset++].getContents();
                    
                } else if (Operators.isOperatorMethod(line[offset].getToken())) {
                    
                    name = line[offset++].getToken().getReadable();
                    
                } else if (offset + 2 < line.length && line[offset].getToken() == Token.LEFT_SQUARE_BRACKET && line[offset + 1].getToken() == Token.RIGHT_SQUARE_BRACKET && line[offset + 2].getToken() == Token.ASSIGNMENT_OP) {
                    
                    name = "[]=";
                    offset += 3;
                    
                } else if (offset + 1 < line.length && line[offset].getToken() == Token.LEFT_SQUARE_BRACKET && line[offset + 1].getToken() == Token.RIGHT_SQUARE_BRACKET) {
                    
                    name = "[]";
                    offset += 2;
                    
                } else {
                    
                    Errors.reportSyntaxError("Invalid token " + line[offset].getToken().toString() + " in module declaration.", interpreter.getSourceEntry(), location.getLineNumber(), line[offset].getIndex());
                    name = "";
                }
                
                Parameters parameters = new Parameters(new ArrayList<>(), new LinkedHashMap<>(), null, null);
                
                if (offset + 1 < line.length && line[offset].getToken() == Token.LEFT_PARENTHESIS && line[line.length - 1].getToken() == Token.RIGHT_PARENTHESIS) {
                    
                    SourceToken[] paramTokens = new SourceToken[line.length - offset - 2];
                    System.arraycopy(line, offset + 1, paramTokens, 0, paramTokens.length);
                    parameters = interpreter.interpretParameters(paramTokens, location);
                }
                
                return new NativeMethodDeclarationInstructionSet(name, isStatic, isSecure, isFinal, parameters, location);
            }
            
            private boolean leadsWithName(SourceToken[] line, int offset) {
                
                if (line[offset].getToken() == Token.TOKEN_STRING) {
                    
                    return true;
                    
                } else if (Operators.isOperatorMethod(line[offset].getToken())) {
                    
                    return true;
                    
                } else if (offset + 2 < line.length && line[offset].getToken() == Token.LEFT_SQUARE_BRACKET && line[offset + 1].getToken() == Token.RIGHT_SQUARE_BRACKET && line[offset + 2].getToken() == Token.ASSIGNMENT_OP) {
                    
                    return true;
                    
                } else if (offset + 1 < line.length && line[offset].getToken() == Token.LEFT_SQUARE_BRACKET && line[offset + 1].getToken() == Token.RIGHT_SQUARE_BRACKET) {
                    
                    return true;
                }
                
                return false;
            }
        });
        DeclarationExpressions.registerDeclarationExpression(Token.VAR, new DeclarationExpression() {
            
            @Override
            public InstructionSet interpret(SourceToken[] line, Block next, Interpreter interpreter, Location location) {
                
                return parseVariableDeclaration(line, next, interpreter, false, location);
            }
        });
        DeclarationExpressions.registerDeclarationExpression(Token.VAL, new DeclarationExpression() {
            
            @Override
            public InstructionSet interpret(SourceToken[] line, Block next, Interpreter interpreter, Location location) {
                
                return parseVariableDeclaration(line, next, interpreter, true, location);
            }
        });
    }
    
    private static InstructionSet parseVariableDeclaration(SourceToken[] line, Block next, Interpreter interpreter, boolean constant, Location location) {
        
        boolean isNative = false, isStatic = false;
        
        int offset = 0;
        while (line[offset].getToken() != Token.TOKEN_STRING) {
            
            switch (line[offset++].getToken()) {
                
                case NATIVE:
                    
                    isNative = true;
                    break;
                
                case STATIC:
                    
                    isStatic = true;
                    break;
                
                default:
                    
                    Errors.reportSyntaxError("Invalid token " + line[offset].getToken().toString() + " in field declaration.", interpreter.getSourceEntry(), location.getLineNumber(), line[offset].getIndex());
            }
        }
        
        SourceToken[] strippedTokens = new SourceToken[line.length - offset];
        System.arraycopy(line, offset, strippedTokens, 0, strippedTokens.length);
        
        List<List<SourceToken>> splitDeclaration = interpreter.splitTokens(strippedTokens, Token.COMMA, -1);
        
        Instruction[] instructions = new Instruction[splitDeclaration.size()];
        for (int i = 0; i < splitDeclaration.size(); i++) {
            
            List<SourceToken> tokens = splitDeclaration.get(i);
            
            String name = null;
            if (tokens.get(0).getToken() == Token.TOKEN_STRING) {
                
                name = tokens.get(0).getContents();
                
            } else {
                
                Errors.reportSyntaxError("Invalid token " + tokens.get(0).getToken().toString() + " in field declaration.  Expected " + Token.TOKEN_STRING.toString() + ".", interpreter.getSourceEntry(), location.getLineNumber(), tokens.get(0).getIndex());
            }
            
            ProcedureAction action = null;
            if (1 < tokens.size() && tokens.get(1).getToken() == Token.ASSIGNMENT_OP) {
                
                SourceToken[] strippedValue = new SourceToken[tokens.size() - 2];
                System.arraycopy(tokens.toArray(new SourceToken[tokens.size()]), 2, strippedValue, 0, strippedValue.length);
                
                InstructionSet value = interpreter.interpretExpression(null, strippedValue, location, i == splitDeclaration.size() - 1 ? next : null);
                action = new VariableProcedureAction(value, location);
            }
            
            instructions[i] = new VariableDeclarationInstruction(name, isNative, isStatic, constant, action, location);
        }
        
        return new InstructionSet(instructions, location);
    }
}
