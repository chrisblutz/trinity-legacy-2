package com.github.chrisblutz.trinity.interpreter.facets;

import com.github.chrisblutz.trinity.interpreter.Interpreter;
import com.github.chrisblutz.trinity.interpreter.KeywordExpressions;
import com.github.chrisblutz.trinity.interpreter.Location;
import com.github.chrisblutz.trinity.interpreter.components.KeywordExpression;
import com.github.chrisblutz.trinity.interpreter.components.KeywordExpressionConstraint;
import com.github.chrisblutz.trinity.interpreter.instructions.*;
import com.github.chrisblutz.trinity.lang.errors.Errors;
import com.github.chrisblutz.trinity.lang.procedures.ProcedureAction;
import com.github.chrisblutz.trinity.parser.tokens.SourceToken;
import com.github.chrisblutz.trinity.parser.tokens.Token;


/**
 * @author Christopher Lutz
 */
public class KeywordExpressionFacets {
    
    public static void registerFacets() {
        
        KeywordExpressions.registerKeywordExpression(Token.SCOPE_MODIFIER, new KeywordExpression(0, true, true, null) {
            
            @Override
            public InstructionSet interpret(SourceToken token, InstructionSet[] sets, ProcedureAction next, Interpreter interpreter, Location location) {
                
                return new ScopeModifierInstructionSet(token.getContents(), next, location);
            }
        });
        
        KeywordExpressions.registerKeywordExpression(Token.REQUIRE, new KeywordExpression(1, true, true, null) {
            
            @Override
            public InstructionSet interpret(SourceToken token, InstructionSet[] sets, ProcedureAction next, Interpreter interpreter, Location location) {
                
                return new RequireInstructionSet(sets[0], location);
            }
        });
        KeywordExpressions.registerKeywordExpression(Token.USING, new KeywordExpression(1, true, true, null) {
            
            @Override
            public InstructionSet interpret(SourceToken token, InstructionSet[] sets, ProcedureAction next, Interpreter interpreter, Location location) {
                
                return new UsingInstructionSet(sets[0], location);
            }
        });
        
        KeywordExpressions.registerKeywordExpression(Token.IF, new KeywordExpression(1, true, true, null) {
            
            @Override
            public InstructionSet interpret(SourceToken token, InstructionSet[] sets, ProcedureAction next, Interpreter interpreter, Location location) {
                
                return new IfInstructionSet(Token.IF, sets[0], next, location);
            }
        });
        KeywordExpressions.registerKeywordExpression(Token.ELSIF, new KeywordExpression(1, true, false, null) {
            
            @Override
            public InstructionSet interpret(SourceToken token, InstructionSet[] sets, ProcedureAction next, Interpreter interpreter, Location location) {
                
                return new IfInstructionSet(Token.ELSIF, sets[0], next, location);
            }
        });
        KeywordExpressions.getExpression(Token.ELSIF).setConstraint(new KeywordExpressionConstraint(new Token[]{Token.IF, Token.ELSIF}, "Invalid 'elsif' statement placement.") {
            
            @Override
            public boolean check(InstructionSet set, InstructionSet previous) {
                
                if (set instanceof IfInstructionSet && previous instanceof IfInstructionSet) {
                    
                    ((IfInstructionSet) previous).setChild((IfInstructionSet) set);
                    return true;
                    
                } else {
                    
                    return false;
                }
            }
        });
        KeywordExpressions.registerKeywordExpression(Token.ELSE, new KeywordExpression(0, true, false, null) {
            
            @Override
            public InstructionSet interpret(SourceToken token, InstructionSet[] sets, ProcedureAction next, Interpreter interpreter, Location location) {
                
                return new IfInstructionSet(Token.ELSE, null, next, location);
            }
        });
        KeywordExpressions.getExpression(Token.ELSE).setConstraint(new KeywordExpressionConstraint(new Token[]{Token.IF, Token.ELSIF}, "Invalid 'else' statement placement.") {
            
            @Override
            public boolean check(InstructionSet set, InstructionSet previous) {
                
                if (set instanceof IfInstructionSet && previous instanceof IfInstructionSet) {
                    
                    ((IfInstructionSet) previous).setChild((IfInstructionSet) set);
                    return true;
                    
                } else {
                    
                    return false;
                }
            }
        });
        
        KeywordExpressions.registerKeywordExpression(Token.WHILE, new KeywordExpression(1, true, true, null) {
            
            @Override
            public InstructionSet interpret(SourceToken token, InstructionSet[] sets, ProcedureAction next, Interpreter interpreter, Location location) {
                
                return new WhileInstructionSet(sets[0], next, location);
            }
        });
        KeywordExpressions.registerKeywordExpression(Token.FOR, new KeywordExpression(3, true, true, Token.SEMICOLON) {
            
            @Override
            public InstructionSet interpret(SourceToken token, InstructionSet[] sets, ProcedureAction next, Interpreter interpreter, Location location) {
                
                return new ForInstructionSet(sets[0], sets[1], sets[2], next, location);
            }
        });
        
        KeywordExpressions.registerKeywordExpression(Token.SWITCH, new KeywordExpression(1, true, true, null) {
            
            @Override
            public InstructionSet interpret(SourceToken token, InstructionSet[] sets, ProcedureAction next, Interpreter interpreter, Location location) {
                
                return new SwitchInstructionSet(Token.SWITCH, sets[0], next, location);
            }
        });
        KeywordExpressions.registerKeywordExpression(Token.CASE, new KeywordExpression(1, true, false, null) {
            
            @Override
            public InstructionSet interpret(SourceToken token, InstructionSet[] sets, ProcedureAction next, Interpreter interpreter, Location location) {
                
                return new SwitchInstructionSet(Token.CASE, sets[0], next, location);
            }
        });
        KeywordExpressions.getExpression(Token.CASE).setConstraint(new KeywordExpressionConstraint(new Token[]{Token.SWITCH, Token.CASE}, "Invalid 'case' statement placement.") {
            
            @Override
            public boolean check(InstructionSet set, InstructionSet previous) {
                
                if (set instanceof SwitchInstructionSet && previous instanceof SwitchInstructionSet) {
                    
                    ((SwitchInstructionSet) previous).setChild((SwitchInstructionSet) set);
                    return true;
                    
                } else {
                    
                    return false;
                }
            }
        });
        KeywordExpressions.registerKeywordExpression(Token.DEFAULT, new KeywordExpression(0, true, false, null) {
            
            @Override
            public InstructionSet interpret(SourceToken token, InstructionSet[] sets, ProcedureAction next, Interpreter interpreter, Location location) {
                
                return new SwitchInstructionSet(Token.DEFAULT, null, next, location);
            }
        });
        KeywordExpressions.getExpression(Token.DEFAULT).setConstraint(new KeywordExpressionConstraint(new Token[]{Token.SWITCH, Token.CASE}, "Invalid 'default' statement placement.") {
            
            @Override
            public boolean check(InstructionSet set, InstructionSet previous) {
                
                if (set instanceof SwitchInstructionSet && previous instanceof SwitchInstructionSet) {
                    
                    ((SwitchInstructionSet) previous).setChild((SwitchInstructionSet) set);
                    return true;
                    
                } else {
                    
                    return false;
                }
            }
        });
        
        KeywordExpressions.registerKeywordExpression(Token.RETURN, new KeywordExpression(1, false, true, null) {
            
            @Override
            public InstructionSet interpret(SourceToken token, InstructionSet[] sets, ProcedureAction next, Interpreter interpreter, Location location) {
                
                return new ReturnInstructionSet(sets[0], location);
            }
        });
        
        KeywordExpressions.registerKeywordExpression(Token.TRY, new KeywordExpression(0, true, true, null) {
            
            @Override
            public InstructionSet interpret(SourceToken token, InstructionSet[] sets, ProcedureAction next, Interpreter interpreter, Location location) {
                
                return new TryInstructionSet(next, location);
            }
        });
        KeywordExpressions.registerKeywordExpression(Token.CATCH, new KeywordExpression(1, true, false, null) {
            
            @Override
            public InstructionSet interpret(SourceToken token, InstructionSet[] sets, ProcedureAction next, Interpreter interpreter, Location location) {
                
                if (sets[0].getInstructions().length == 1 && sets[0].getInstructions()[0] instanceof SingleTokenInstruction) {
                    
                    String name = ((SingleTokenInstruction) sets[0].getInstructions()[0]).getContents();
                    return new CatchInstructionSet(name, next, location);
                    
                } else {
                    
                    Errors.reportSyntaxError("Invalid 'catch' statement error variable.", interpreter.getSourceEntry(), location.getLineNumber(), 0);
                    return null;
                }
            }
        });
        KeywordExpressions.getExpression(Token.CATCH).setConstraint(new KeywordExpressionConstraint(new Token[]{Token.TRY}, "Invalid 'catch' statement placement.") {
            
            @Override
            public boolean check(InstructionSet set, InstructionSet previous) {
                
                if (set instanceof CatchInstructionSet && previous instanceof TryInstructionSet) {
                    
                    ((TryInstructionSet) previous).setCatchSet((CatchInstructionSet) set);
                    ((CatchInstructionSet) set).setTrySet((TryInstructionSet) previous);
                    return true;
                    
                } else {
                    
                    return false;
                }
            }
        });
        KeywordExpressions.registerKeywordExpression(Token.FINALLY, new KeywordExpression(0, true, false, null) {
            
            @Override
            public InstructionSet interpret(SourceToken token, InstructionSet[] sets, ProcedureAction next, Interpreter interpreter, Location location) {
                
                return new FinallyInstructionSet(next, location);
            }
        });
        KeywordExpressions.getExpression(Token.FINALLY).setConstraint(new KeywordExpressionConstraint(new Token[]{Token.TRY, Token.CATCH}, "Invalid 'finally' statement placement.") {
            
            @Override
            public boolean check(InstructionSet set, InstructionSet previous) {
                
                if (set instanceof FinallyInstructionSet) {
                    
                    if (previous instanceof TryInstructionSet) {
                        
                        ((TryInstructionSet) previous).setFinallySet((FinallyInstructionSet) set);
                        
                    } else if (previous instanceof CatchInstructionSet) {
                        
                        ((CatchInstructionSet) previous).getTrySet().setFinallySet((FinallyInstructionSet) set);
                    }
                }
                
                return false;
            }
        });
    }
}
