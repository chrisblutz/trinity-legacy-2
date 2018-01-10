package com.github.chrisblutz.trinity.interpreter;

import com.github.chrisblutz.trinity.interpreter.actions.ArgumentProcedureAction;
import com.github.chrisblutz.trinity.interpreter.actions.ExpressionProcedureAction;
import com.github.chrisblutz.trinity.interpreter.components.DeclarationExpression;
import com.github.chrisblutz.trinity.interpreter.components.KeywordExpression;
import com.github.chrisblutz.trinity.interpreter.instructions.*;
import com.github.chrisblutz.trinity.lang.errors.Errors;
import com.github.chrisblutz.trinity.lang.procedures.ProcedureAction;
import com.github.chrisblutz.trinity.lang.procedures.TyProcedure;
import com.github.chrisblutz.trinity.parser.SourceEntry;
import com.github.chrisblutz.trinity.parser.blocks.Block;
import com.github.chrisblutz.trinity.parser.blocks.Statement;
import com.github.chrisblutz.trinity.parser.tokens.SourceToken;
import com.github.chrisblutz.trinity.parser.tokens.Token;

import java.util.*;


/**
 * @author Christopher Lutz
 */
public class Interpreter {
    
    private Block block;
    private SourceEntry sourceEntry;
    
    public Interpreter(Block block) {
        
        this.block = block;
        this.sourceEntry = block.getSourceEntry();
    }
    
    public Block getBlock() {
        
        return block;
    }
    
    public SourceEntry getSourceEntry() {
        
        return sourceEntry;
    }
    
    public ProcedureAction interpret() {
        
        return interpret(getBlock(), true);
    }
    
    public ProcedureAction interpret(Block block, boolean appendToStackTrace) {
        
        List<InstructionSet> sets = new ArrayList<>();
        for (int i = 0; i < block.size(); i++) {
            
            Statement statement = (Statement) block.get(i);
            
            Block nextBlock = null;
            if (i + 1 < block.size() && block.get(i + 1) instanceof Block) {
                
                nextBlock = (Block) block.get(++i);
            }
            
            if (statement.size() > 0) {
                
                InstructionSet set = interpretExpression(block, statement.toArray(new SourceToken[statement.size()]), new Location(block.getSourceEntry().getFileName(), block.getSourceEntry().getFilePath(), statement.getLineNumber()), nextBlock);
                if (set != null) {
                    
                    sets.add(set);
                }
            }
        }
        
        return new ExpressionProcedureAction(sets.toArray(new InstructionSet[sets.size()]), appendToStackTrace);
    }
    
    public InstructionSet interpretExpression(Block block, SourceToken[] tokens, Location location, Block nextBlock) {
        
        Token first = tokens[0].getToken();
        
        if (DeclarationExpressions.isDeclarationKeyword(first)) {
            
            SourceToken[] line = new SourceToken[tokens.length - 1];
            System.arraycopy(tokens, 1, line, 0, line.length);
            
            DeclarationExpression expression = DeclarationExpressions.getExpression(first);
            return expression.interpret(line, nextBlock, this, location);
            
        } else if (KeywordExpressions.isKeyword(first)) {
            
            ProcedureAction next = null;
            if (nextBlock != null) {
                
                next = interpret(nextBlock, false);
            }
            
            KeywordExpression expression = KeywordExpressions.getExpression(first);
            
            if (expression.checkConstraint(block)) {
                
                SourceToken[] strippedTokens = new SourceToken[tokens.length - 1];
                System.arraycopy(tokens, 1, strippedTokens, 0, strippedTokens.length);
                
                if (strippedTokens.length > 0 && strippedTokens[0].getToken() == Token.LEFT_PARENTHESIS && tokens[tokens.length - 1].getToken() == Token.RIGHT_PARENTHESIS && checkWrapping(strippedTokens)) {
                    
                    SourceToken[] temp = new SourceToken[strippedTokens.length - 2];
                    System.arraycopy(strippedTokens, 1, temp, 0, temp.length);
                    strippedTokens = temp;
                }
                
                int componentCount = expression.getComponentCount();
                List<InstructionSet> components = new ArrayList<>();
                if (componentCount > 0) {
                    
                    if (strippedTokens.length > 0) {
                        
                        Token delimiter = expression.getDelimiter();
                        if (delimiter == null) {
                            
                            components.add(interpretCompoundExpression(strippedTokens, location, null));
                            
                        } else {
                            
                            InstructionSet[] sets = splitExpressions(strippedTokens, delimiter, location, null);
                            components.addAll(Arrays.asList(sets));
                        }
                        
                        if (components.size() > componentCount || (components.size() < componentCount && expression.isRigid())) {
                            
                            throwArgumentCountError(first, components.size(), location);
                        }
                        
                    } else if (expression.isRigid()) {
                        
                        throwArgumentCountError(first, components.size(), location);
                    }
                    
                } else if (strippedTokens.length > 0) {
                    
                    throwArgumentCountError(first, components.size(), location);
                }
                
                InstructionSet[] sets = components.toArray(new InstructionSet[components.size()]);
                InstructionSet thisSet = expression.interpret(tokens[0], sets, next, this, location);
                
                if (expression.runConstraint(block, thisSet)) {
                    
                    KeywordExpressions.updatePrevious(block, first, thisSet);
                    
                    if (expression.isAddDirect()) {
                        
                        return thisSet;
                        
                    } else {
                        
                        return null;
                    }
                    
                } else {
                    
                    Errors.reportSyntaxError(expression.getConstraint().getMessage(), getSourceEntry(), location.getLineNumber(), -1);
                }
                
            } else {
                
                Errors.reportSyntaxError(expression.getConstraint().getMessage(), getSourceEntry(), location.getLineNumber(), -1);
            }
            
        } else {
            
            TyProcedure next = null;
            if (nextBlock != null) {
                
                ProcedureAction action = interpret(nextBlock, true);
                
                if (nextBlock.hasHeader()) {
                    
                    Parameters parameters = interpretParameters(nextBlock.getHeader().toArray(new SourceToken[nextBlock.getHeader().size()]), location);
                    next = new TyProcedure(action, parameters.getMandatoryParameters(), parameters.getOptionalParameters(), parameters.getBlockParameter(), parameters.getOverflowParameter(), false);
                    
                } else {
                    
                    next = new TyProcedure(action, false);
                }
            }
            
            return interpretCompoundExpression(tokens, location, next);
        }
        
        return null;
    }
    
    private void throwArgumentCountError(Token token, int count, Location location) {
        
        Errors.reportSyntaxError("'" + token.getLiteral() + "' statements require " + count + " expressions, found " + count + ".", getSourceEntry(), location.getLineNumber(), -1);
    }
    
    private boolean checkWrapping(SourceToken[] tokens) {
        
        int level = 0;
        for (int i = 0; i < tokens.length; i++) {
            
            Token t = tokens[i].getToken();
            if (t == Token.LEFT_PARENTHESIS) {
                
                level++;
                
            } else if (t == Token.RIGHT_PARENTHESIS) {
                
                level--;
            }
            
            if (level == 0 && i < tokens.length - 1) {
                
                return false;
            }
        }
        
        return true;
    }
    
    private InstructionSet interpretCompoundExpression(SourceToken[] tokens, Location location, TyProcedure next) {
        
        Token[] logicalOperators = LogicalOperator.getOperators().toArray(new Token[LogicalOperator.getOperators().size()]);
        
        if (containsOnFirstLevel(tokens, AssignmentOperators.getAssignmentTokens())) {
            
            // Signifies assignment (ex. x = 10)
            if (containsOnFirstLevel(tokens, Token.COMMA)) {
                
                InstructionSet[] sets = splitExpressions(tokens, Token.COMMA, location, next);
                return new InstructionSet(sets, location);
                
            } else {
                
                Token token = findOnFirstLevel(tokens, AssignmentOperators.getAssignmentTokens());
                
                List<List<SourceToken>> tokenSets = splitTokens(tokens, token, 1);
                SourceToken[] assignmentTokens = tokenSets.get(0).toArray(new SourceToken[tokenSets.get(0).size()]);
                SourceToken[] valueTokens = tokenSets.get(1).toArray(new SourceToken[tokenSets.get(1).size()]);
                InstructionSet value = interpretCompoundExpression(valueTokens, location, next);
                
                if (assignmentTokens[assignmentTokens.length - 1].getToken() == Token.RIGHT_SQUARE_BRACKET) {
                    
                    int loc = findBracketBeginning(tokens, location);
                    SourceToken[] strippedTokens = new SourceToken[assignmentTokens.length - loc - 2];
                    System.arraycopy(assignmentTokens, loc + 1, strippedTokens, 0, strippedTokens.length);
                    
                    InstructionSet[] indices = splitExpressions(strippedTokens, Token.COMMA, location, null);
                    
                    SourceToken[] objectTokens = new SourceToken[loc];
                    System.arraycopy(tokens, 0, objectTokens, 0, objectTokens.length);
                    
                    InstructionSet object = interpretCompoundExpression(objectTokens, location, null);
                    
                    return new InstructionSet(new Instruction[]{new IndexAssignmentInstruction(token, object, indices, value, location)}, location);
                    
                } else {
                    
                    InstructionSet assignmentObject = interpretCompoundExpression(assignmentTokens, location, null);
                    
                    if (assignmentObject != null) {
                        
                        Instruction[] instructions = assignmentObject.getInstructions();
                        Instruction[] remainder = new Instruction[instructions.length - 1];
                        System.arraycopy(instructions, 0, remainder, 0, remainder.length);
                        Instruction end = instructions[instructions.length - 1];
                        
                        if (end instanceof InstructionSet && ((InstructionSet) end).getInstructions().length == 1) {
                            
                            end = ((InstructionSet) end).getInstructions()[0];
                        }
                        
                        InstructionSet remainderSet = new InstructionSet(remainder, assignmentObject.getLocation());
                        VariableLocationRetriever retriever = null;
                        if (end instanceof GlobalVariableInstruction) {
                            
                            retriever = new GlobalVariableLocationRetriever(((GlobalVariableInstruction) end).getName());
                            
                        } else if (end instanceof SingleTokenInstruction) {
                            
                            retriever = new SingleTokenVariableLocationRetriever(((SingleTokenInstruction) end).getContents());
                            
                        } else {
                            
                            Errors.reportSyntaxError("Invalid left-hand assignment object.", getSourceEntry(), location.getLineNumber(), tokens[0].getIndex());
                        }
                        
                        return new InstructionSet(new Instruction[]{new AssignmentInstruction(token, remainderSet, retriever, value, location)}, location);
                    }
                }
            }
            
        } else if (containsOnFirstLevelSequentially(tokens, Token.QUESTION_MARK, Token.COLON)) {
            
            // Signifies ternary operator usage (ex. x ? y : z)
            List<List<SourceToken>> firstSplit = splitTokens(tokens, Token.QUESTION_MARK, 1);
            List<SourceToken> firstHalfList = firstSplit.get(0);
            SourceToken[] firstHalf = firstHalfList.toArray(new SourceToken[firstHalfList.size()]);
            
            List<SourceToken> secondHalfList = firstSplit.get(1);
            SourceToken[] secondHalf = secondHalfList.toArray(new SourceToken[secondHalfList.size()]);
            
            InstructionSet first = interpretCompoundExpression(firstHalf, location, next);
            InstructionSet[] secondSplit = splitByToken(secondHalf, Token.COLON, location, next);
            
            return new InstructionSet(new Instruction[]{new TernaryOperatorInstruction(first, secondSplit[0], secondSplit[1], location)}, location);
            
        } else if (containsOnFirstLevel(tokens, logicalOperators)) {
            
            // Signifies binary logical operator usage (ex. x && y)
            Token token = findOnFirstLevel(tokens, logicalOperators);
            
            InstructionSet[] sets = splitByToken(tokens, token, location, next);
            
            return new InstructionSet(new Instruction[]{sets[0], new LogicalOperatorInstruction(LogicalOperator.getOperator(token), sets[1], location)}, location);
            
        } else if (containsOperatorOnFirstLevel(tokens)) {
            
            // Signifies binary operator usage (ex. 1 + 2)
            Token token = findOperatorOnFirstLevel(tokens);
            
            InstructionSet[] sets = splitByTokenWithOperators(tokens, token, location, next);
            
            return new InstructionSet(new Instruction[]{sets[0], new BinaryOperatorInstruction(BinaryOperator.getOperator(token), sets[1], location)}, location);
            
        } else if (containsOnFirstLevel(tokens, Token.DOUBLE_DOT_OP, Token.TRIPLE_DOT_OP)) {
            
            // Signifies range initialization (ex. 1..10)
            Token token = findOnFirstLevel(tokens, Token.DOUBLE_DOT_OP, Token.TRIPLE_DOT_OP);
            
            InstructionSet[] sets = splitByToken(tokens, token, location, next);
            
            return new InstructionSet(new Instruction[]{new RangeCreationInstruction(token, sets[0], sets[1], location)}, location);
            
        } else if (tokens.length > 0 && UnaryOperator.getOperators().contains(tokens[0].getToken())) {
            
            // Signifies non-arithmetic unary operator use (ex. !true)
            Token token = tokens[0].getToken();
            SourceToken[] strippedTokens = new SourceToken[tokens.length - 1];
            System.arraycopy(tokens, 1, strippedTokens, 0, strippedTokens.length);
            
            InstructionSet set = interpretCompoundExpression(strippedTokens, location, next);
            
            return new InstructionSet(new Instruction[]{set, new UnaryOperatorInstruction(UnaryOperator.getOperator(token), location)}, location);
            
        } else if (containsOnFirstLevel(tokens, Token.DOT_OP)) {
            
            // Signifies expressions separated by a dot operator (ex. X.y)
            InstructionSet[] sets = splitByToken(tokens, Token.DOT_OP, location, next);
            
            return new InstructionSet(new Instruction[]{sets[0], sets[1]}, location);
            
        } else if (tokens.length > 0 && ArithmeticUnaryOperator.getOperators().contains(tokens[0].getToken())) {
            
            // Signifies arithmetic unary operator use (ex. -10)
            Token token = tokens[0].getToken();
            SourceToken[] strippedTokens = new SourceToken[tokens.length - 1];
            System.arraycopy(tokens, 1, strippedTokens, 0, strippedTokens.length);
            
            InstructionSet set = interpretCompoundExpression(strippedTokens, location, next);
            
            return new InstructionSet(new Instruction[]{set, new ArithmeticUnaryOperatorInstruction(ArithmeticUnaryOperator.getOperator(token), location)}, location);
            
        } else if (tokens.length == 1 && Keywords.isKeyword(tokens[0].getToken())) {
            
            // Signifies keywords (super, nil, etc.) as well as literal and numeric strings
            return new InstructionSet(new Instruction[]{new KeywordInstruction(tokens[0], location)}, location);
            
        } else {
            
            int loc;
            if (tokens.length > 0 && tokens[tokens.length - 1].getToken() == Token.RIGHT_SQUARE_BRACKET && ((loc = findBracketBeginning(tokens, location)) > 0)) {
                
                // Signifies a [] call on an object
                SourceToken[] strippedTokens = new SourceToken[tokens.length - loc - 2];
                System.arraycopy(tokens, loc + 1, strippedTokens, 0, strippedTokens.length);
                
                InstructionSet[] indices = splitExpressions(strippedTokens, Token.COMMA, location, null);
                
                SourceToken[] objectTokens = new SourceToken[loc];
                System.arraycopy(tokens, 0, objectTokens, 0, objectTokens.length);
                
                InstructionSet object = interpretCompoundExpression(objectTokens, location, null);
                
                return new InstructionSet(new Instruction[]{object, new IndexAccessInstruction(indices, next, location)}, location);
                
            } else if (tokens.length > 0 && tokens[0].getToken() == Token.LEFT_PARENTHESIS) {
                
                // Signifies an expression wrapped in parentheses
                checkWrapping(tokens, Token.LEFT_PARENTHESIS, Token.RIGHT_PARENTHESIS, "Unmatched parentheses.", location);
                
                SourceToken[] strippedTokens = new SourceToken[tokens.length - 2];
                System.arraycopy(tokens, 1, strippedTokens, 0, strippedTokens.length);
                
                return interpretCompoundExpression(strippedTokens, location, null);
                
            } else if (tokens.length > 0 && tokens[0].getToken() == Token.LEFT_SQUARE_BRACKET) {
                
                // Signifies array initialization
                checkWrapping(tokens, Token.LEFT_SQUARE_BRACKET, Token.RIGHT_SQUARE_BRACKET, "Unmatched brackets.", location);
                
                SourceToken[] strippedTokens = new SourceToken[tokens.length - 2];
                System.arraycopy(tokens, 1, strippedTokens, 0, strippedTokens.length);
                
                InstructionSet[] arrayComponents = splitExpressions(strippedTokens, Token.COMMA, location, null);
                
                return new InstructionSet(new Instruction[]{new ArrayInitializationInstruction(arrayComponents, location)}, location);
                
            } else if (tokens.length > 0 && tokens[0].getToken() == Token.LEFT_CURLY_BRACKET) {
                
                // Signifies map initialization
                checkWrapping(tokens, Token.LEFT_CURLY_BRACKET, Token.RIGHT_CURLY_BRACKET, "Unmatched braces.", location);
                
                SourceToken[] strippedTokens = new SourceToken[tokens.length - 2];
                System.arraycopy(tokens, 1, strippedTokens, 0, strippedTokens.length);
                
                List<List<SourceToken>> tokenSets = splitTokens(strippedTokens, Token.COMMA, -1);
                List<InstructionSet[]> mapComponents = new ArrayList<>();
                
                for (List<SourceToken> set : tokenSets) {
                    
                    SourceToken[] array = set.toArray(new SourceToken[set.size()]);
                    mapComponents.add(splitByToken(array, Token.COLON, location, null));
                }
                
                return new InstructionSet(new Instruction[]{new MapInitializationInstruction(mapComponents, location)}, location);
                
            } else if (tokens.length == 2 && tokens[0].getToken() == Token.GLOBAL_VARIABLE_PREFIX && tokens[1].getToken() == Token.TOKEN_STRING) {
                
                // Signifies global variables
                return new InstructionSet(new Instruction[]{new GlobalVariableInstruction(tokens[1].getContents(), location)}, location);
                
            } else if (tokens.length == 1 && (tokens[0].getToken() == Token.CLASS || tokens[0].getToken() == Token.MODULE)) {
                
                // Signifies class/module object retrieval (i.e. X.Y.class)
                return new InstructionSet(new Instruction[]{new UsableRetrieverInstruction(tokens[0].getToken(), location)}, location);
                
            } else if (tokens.length == 1 && tokens[0].getToken() == Token.TOKEN_STRING) {
                
                // Signifies single-token name (class/module names, variables, etc.)
                return new InstructionSet(new Instruction[]{new SingleTokenInstruction(tokens[0].getContents(), location)}, location);
                
            } else if (tokens.length > 1 && tokens[tokens.length - 1].getToken() == Token.RIGHT_CURLY_BRACKET && ((loc = findBlockBeginning(tokens, location)) > 0)) {
                
                SourceToken[] blockTokens = new SourceToken[tokens.length - loc - 2];
                System.arraycopy(tokens, loc + 1, blockTokens, 0, blockTokens.length);
                
                Parameters parameters = new Parameters(new ArrayList<>(), new LinkedHashMap<>(), null, null);
                if (blockTokens.length > 0 && blockTokens[0].getToken() == Token.BITWISE_OR_OP) {
                    
                    int level = 0;
                    int i;
                    for (i = 1; i < blockTokens.length; i++) {
                        
                        SourceToken token = blockTokens[i];
                        if (isLevelUpToken(token.getToken())) {
                            
                            level++;
                            
                        } else if (isLevelDownToken(token.getToken())) {
                            
                            level--;
                            
                        } else if (level == 0 && token.getToken() == Token.BITWISE_OR_OP) {
                            
                            break;
                        }
                    }
                    
                    SourceToken[] header = new SourceToken[i - 1];
                    System.arraycopy(blockTokens, 1, header, 0, header.length);
                    
                    parameters = interpretParameters(header, location);
                    
                    SourceToken[] temp = new SourceToken[blockTokens.length - i - 1];
                    System.arraycopy(blockTokens, i + 1, temp, 0, temp.length);
                    blockTokens = temp;
                }
                
                Block block = new Block(getSourceEntry());
                Statement statement = new Statement(getSourceEntry(), location.getLineNumber());
                Collections.addAll(statement, blockTokens);
                block.add(statement);
                
                ProcedureAction action = interpret(block, true);
                TyProcedure nextProcedure = new TyProcedure(action, parameters.getMandatoryParameters(), parameters.getOptionalParameters(), parameters.getBlockParameter(), parameters.getOverflowParameter(), false);
                
                SourceToken[] remaining = new SourceToken[loc];
                System.arraycopy(tokens, 0, remaining, 0, remaining.length);
                
                return interpretCompoundExpression(remaining, location, nextProcedure);
                
            } else if (tokens.length > 1 && tokens[1].getToken() == Token.LEFT_PARENTHESIS) {
                
                // Signifies a method call
                SourceToken[] paramTokens = new SourceToken[tokens.length - 1];
                System.arraycopy(tokens, 1, paramTokens, 0, paramTokens.length);
                
                checkWrapping(paramTokens, Token.LEFT_PARENTHESIS, Token.RIGHT_PARENTHESIS, "Unmatched parentheses.", location);
                
                SourceToken[] strippedTokens = new SourceToken[paramTokens.length - 2];
                System.arraycopy(paramTokens, 1, strippedTokens, 0, strippedTokens.length);
                
                InstructionSet[] params = splitExpressions(strippedTokens, Token.COMMA, location, null);
                
                return new InstructionSet(new Instruction[]{new MethodCallInstruction(tokens[0].getContents(), params, next, location)}, location);
                
            } else {
                
                Errors.reportSyntaxError("Unrecognized token.", getSourceEntry(), location.getLineNumber(), tokens[0].getIndex());
            }
        }
        
        return null;
    }
    
    private int findBracketBeginning(SourceToken[] tokens, Location location) {
        
        int level = 0;
        for (int i = tokens.length - 1; i >= 0; i--) {
            
            SourceToken token = tokens[i];
            if (isLevelUpToken(token.getToken())) {
                
                level++;
                
            } else if (isLevelDownToken(token.getToken())) {
                
                level--;
                
            }
            
            if (level == 0 && token.getToken() == Token.LEFT_SQUARE_BRACKET) {
                
                return i;
            }
        }
        
        Errors.reportSyntaxError("Unmatched brackets.", getSourceEntry(), location.getLineNumber(), tokens[tokens.length - 1].getIndex());
        return 0;
    }
    
    private int findBlockBeginning(SourceToken[] tokens, Location location) {
        
        int level = 0;
        for (int i = tokens.length - 1; i >= 0; i--) {
            
            SourceToken token = tokens[i];
            if (isLevelUpToken(token.getToken())) {
                
                level++;
                
            } else if (isLevelDownToken(token.getToken())) {
                
                level--;
                
            }
            
            if (level == 0 && token.getToken() == Token.LEFT_CURLY_BRACKET) {
                
                return i;
            }
        }
        
        Errors.reportSyntaxError("Unmatched braces.", getSourceEntry(), location.getLineNumber(), tokens[tokens.length - 1].getIndex());
        return 0;
    }
    
    private void checkWrapping(SourceToken[] tokens, Token left, Token right, String errorMessage, Location location) {
        
        if (tokens[0].getToken() == left && tokens[tokens.length - 1].getToken() == right) {
            
            // Check for imbalanced brackets
            int level = 0;
            for (SourceToken token : tokens) {
                
                if (isLevelUpToken(token.getToken())) {
                    
                    level++;
                    
                } else if (isLevelDownToken(token.getToken())) {
                    
                    level--;
                }
            }
            
            if (level == 0) {
                
                return;
            }
        }
        
        Errors.reportSyntaxError(errorMessage, getSourceEntry(), location.getLineNumber(), tokens[0].getIndex());
    }
    
    private InstructionSet[] splitExpressions(SourceToken[] tokens, Token delimiter, Location location, TyProcedure next) {
        
        List<List<SourceToken>> tokenSets = splitTokens(tokens, delimiter, -1);
        List<InstructionSet> sets = new ArrayList<>();
        
        for (List<SourceToken> list : tokenSets) {
            
            SourceToken[] listArr = list.toArray(new SourceToken[list.size()]);
            sets.add(interpretCompoundExpression(listArr, location, next));
        }
        
        return sets.toArray(new InstructionSet[sets.size()]);
    }
    
    public List<List<SourceToken>> splitTokens(SourceToken[] tokens, Token delimiter, int limit) {
        
        List<List<SourceToken>> sets = new ArrayList<>();
        
        List<SourceToken> current = new ArrayList<>();
        int level = 0;
        for (SourceToken token : tokens) {
            
            if (level == 0 && token.getToken() == delimiter && sets.size() != limit) {
                
                List<SourceToken> tempSet = new ArrayList<>();
                tempSet.addAll(current);
                sets.add(tempSet);
                current.clear();
                
            } else {
                
                if (isLevelUpToken(token.getToken())) {
                    
                    level++;
                    
                } else if (isLevelDownToken(token.getToken())) {
                    
                    level--;
                }
                
                current.add(token);
            }
        }
        
        if (!current.isEmpty()) {
            
            sets.add(current);
        }
        
        return sets;
    }
    
    private List<List<SourceToken>> splitTokensWithOperators(SourceToken[] tokens, Token delimiter) {
        
        List<List<SourceToken>> sets = new ArrayList<>();
        
        List<SourceToken> current = new ArrayList<>();
        int level = 0;
        for (int i = 0; i < tokens.length; i++) {
            
            SourceToken token = tokens[i];
            if (level == 0 && token.getToken() == delimiter && checkOperator(tokens, i, delimiter)) {
                
                List<SourceToken> tempSet = new ArrayList<>();
                tempSet.addAll(current);
                sets.add(tempSet);
                current.clear();
                
            } else {
                
                if (isLevelUpToken(token.getToken())) {
                    
                    level++;
                    
                } else if (isLevelDownToken(token.getToken())) {
                    
                    level--;
                }
                
                current.add(token);
            }
        }
        
        if (!current.isEmpty()) {
            
            sets.add(current);
        }
        
        return sets;
    }
    
    private boolean checkOperator(SourceToken[] tokens, int i, Token token) {
        
        if (UnaryOperator.getOperators().contains(token) || ArithmeticUnaryOperator.getOperators().contains(token)) {
            
            if (i == 0 || (i - 1 > 0 && BinaryOperator.getOperators().contains(tokens[i - 1].getToken()))) {
                
                return false;
            }
        }
        
        return true;
    }
    
    private InstructionSet[] splitByToken(SourceToken[] tokens, Token delimiter, Location location, TyProcedure next) {
        
        List<List<SourceToken>> tokenSets = splitTokens(tokens, delimiter, -1);
        
        List<SourceToken> firstExpression = new ArrayList<>();
        for (int i = 0; i < tokenSets.size() - 1; i++) {
            
            firstExpression.addAll(tokenSets.get(i));
            if (i < tokenSets.size() - 2) {
                
                int index = tokenSets.get(i + 1).get(0).getIndex() - delimiter.getReadable().length();
                firstExpression.add(new SourceToken(delimiter, location.getLineNumber(), index, delimiter.getLiteral(), getSourceEntry()));
            }
        }
        
        List<SourceToken> secondExpression = tokenSets.get(tokenSets.size() - 1);
        
        InstructionSet first = interpretCompoundExpression(firstExpression.toArray(new SourceToken[firstExpression.size()]), location, null);
        InstructionSet second = interpretCompoundExpression(secondExpression.toArray(new SourceToken[secondExpression.size()]), location, next);
        
        return new InstructionSet[]{first, second};
    }
    
    private InstructionSet[] splitByTokenWithOperators(SourceToken[] tokens, Token delimiter, Location location, TyProcedure next) {
        
        List<List<SourceToken>> tokenSets = splitTokensWithOperators(tokens, delimiter);
        
        List<SourceToken> firstExpression = new ArrayList<>();
        for (int i = 0; i < tokenSets.size() - 1; i++) {
            
            firstExpression.addAll(tokenSets.get(i));
            if (i < tokenSets.size() - 2) {
                
                int index = tokenSets.get(i + 1).get(0).getIndex() - delimiter.getReadable().length();
                firstExpression.add(new SourceToken(delimiter, location.getLineNumber(), index, delimiter.getLiteral(), getSourceEntry()));
            }
        }
        
        List<SourceToken> secondExpression = tokenSets.get(tokenSets.size() - 1);
        
        InstructionSet first = interpretCompoundExpression(firstExpression.toArray(new SourceToken[firstExpression.size()]), location, null);
        InstructionSet second = interpretCompoundExpression(secondExpression.toArray(new SourceToken[secondExpression.size()]), location, next);
        
        return new InstructionSet[]{first, second};
    }
    
    public Parameters interpretParameters(SourceToken[] tokens, Location location) {
        
        List<String> mandatory = new ArrayList<>();
        Map<String, ProcedureAction> optional = new LinkedHashMap<>();
        String block = null;
        String overflow = null;
        
        List<List<SourceToken>> tokenSets = splitTokens(tokens, Token.COMMA, -1);
        
        for (List<SourceToken> list : tokenSets) {
            
            if (list.size() == 1 && list.get(0).getToken() == Token.TOKEN_STRING) {
                
                mandatory.add(list.get(0).getContents());
                
            } else if (list.size() > 2 && list.get(0).getToken() == Token.TOKEN_STRING && list.get(1).getToken() == Token.ASSIGNMENT_OP) {
                
                SourceToken[] fullExp = list.toArray(new SourceToken[list.size()]);
                SourceToken[] optionalValue = new SourceToken[list.size() - 2];
                System.arraycopy(fullExp, 2, optionalValue, 0, optionalValue.length);
                
                InstructionSet value = interpretCompoundExpression(optionalValue, location, null);
                ProcedureAction action = new ArgumentProcedureAction(value);
                
                optional.put(list.get(0).getContents(), action);
                
            } else if (list.size() == 2 && list.get(1).getToken() == Token.TOKEN_STRING) {
                
                String contents = list.get(1).getContents();
                if (list.get(0).getToken() == Token.BITWISE_AND_OP) {
                    
                    block = contents;
                    
                } else if (list.get(0).getToken() == Token.TRIPLE_DOT_OP) {
                    
                    overflow = contents;
                }
                
            } else {
                
                Errors.reportSyntaxError("Unrecognized argument style.", getSourceEntry(), location.getLineNumber(), tokens[0].getIndex());
            }
        }
        
        return new Parameters(mandatory, optional, block, overflow);
    }
    
    public static boolean isLevelUpToken(Token t) {
        
        return t == Token.LEFT_PARENTHESIS || t == Token.LEFT_SQUARE_BRACKET || t == Token.LEFT_CURLY_BRACKET;
    }
    
    public static boolean isLevelDownToken(Token t) {
        
        return t == Token.RIGHT_PARENTHESIS || t == Token.RIGHT_SQUARE_BRACKET || t == Token.RIGHT_CURLY_BRACKET;
    }
    
    private boolean containsOnFirstLevel(SourceToken[] tokens, Token... delimiters) {
        
        List<Token> tokenList = Arrays.asList(delimiters);
        
        int level = 0;
        for (SourceToken token : tokens) {
            
            if (isLevelUpToken(token.getToken())) {
                
                level++;
                
            } else if (isLevelDownToken(token.getToken())) {
                
                level--;
                
            } else if (level == 0 && tokenList.contains(token.getToken())) {
                
                return true;
            }
        }
        
        return false;
    }
    
    private Token findOnFirstLevel(SourceToken[] tokens, Token... delimiters) {
        
        List<Token> tokenList = Arrays.asList(delimiters);
        
        int level = 0;
        for (SourceToken token : tokens) {
            
            if (isLevelUpToken(token.getToken())) {
                
                level++;
                
            } else if (isLevelDownToken(token.getToken())) {
                
                level--;
                
            } else if (level == 0 && tokenList.contains(token.getToken())) {
                
                return token.getToken();
            }
        }
        
        return null;
    }
    
    private boolean containsOnFirstLevelSequentially(SourceToken[] tokens, Token first, Token second) {
        
        boolean foundFirst = false;
        
        int level = 0;
        for (SourceToken token : tokens) {
            
            if (isLevelUpToken(token.getToken())) {
                
                level++;
                
            } else if (isLevelDownToken(token.getToken())) {
                
                level--;
                
            } else if (level == 0 && !foundFirst && token.getToken() == first) {
                
                foundFirst = true;
                
            } else if (level == 0 && foundFirst && token.getToken() == second) {
                
                return true;
            }
        }
        
        return false;
    }
    
    private boolean containsOperatorOnFirstLevel(SourceToken[] tokens) {
        
        List<Token> tokenList = BinaryOperator.getOperators();
        
        int level = 0;
        for (int i = 0; i < tokens.length; i++) {
            
            SourceToken token = tokens[i];
            
            if (isLevelUpToken(token.getToken())) {
                
                level++;
                
            } else if (isLevelDownToken(token.getToken())) {
                
                level--;
                
            } else if (level == 0 && tokenList.contains(token.getToken()) && checkOperator(tokens, i, token.getToken())) {
                
                return true;
            }
        }
        
        return false;
    }
    
    private Token findOperatorOnFirstLevel(SourceToken[] tokens) {
        
        List<Token> tokenList = BinaryOperator.getOperators();
        
        int level = 0;
        int precedenceIndex = tokenList.size();
        for (int i = 0; i < tokens.length; i++) {
            
            SourceToken token = tokens[i];
            
            if (isLevelUpToken(token.getToken())) {
                
                level++;
                
            } else if (isLevelDownToken(token.getToken())) {
                
                level--;
                
            } else if (level == 0 && tokenList.contains(token.getToken()) && checkOperator(tokens, i, token.getToken())) {
                
                // Order of operators
                int index = tokenList.indexOf(token.getToken());
                if (index < precedenceIndex) {
                    
                    precedenceIndex = index;
                }
            }
        }
        
        if (precedenceIndex < tokenList.size()) {
            
            return tokenList.get(precedenceIndex);
        }
        
        return null;
    }
}
