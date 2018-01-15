package com.github.chrisblutz.trinity.interpreter.facets;

import com.github.chrisblutz.trinity.interpreter.*;
import com.github.chrisblutz.trinity.interpreter.instructions.InstructionSet;
import com.github.chrisblutz.trinity.lang.TyObject;
import com.github.chrisblutz.trinity.lang.TyRuntime;
import com.github.chrisblutz.trinity.lang.types.TyBoolean;
import com.github.chrisblutz.trinity.lang.types.TyInt;
import com.github.chrisblutz.trinity.natives.TrinityNatives;
import com.github.chrisblutz.trinity.natives.math.TrinityMath;
import com.github.chrisblutz.trinity.parser.tokens.Token;


/**
 * @author Christopher Lutz
 */
public class OperatorFacets {
    
    public static void registerFacets() {
        
        // Logical Operators
        new LogicalOperator(Token.LOGICAL_AND_OP) {
            
            @Override
            public TyBoolean operate(TyObject first, InstructionSet second, TyRuntime runtime) {
                
                if (TrinityNatives.toBoolean(first)) {
                    
                    return TyBoolean.valueFor(TrinityNatives.toBoolean(second.evaluate(TyObject.NONE, runtime)));
                    
                } else {
                    
                    return TyBoolean.FALSE;
                }
            }
        };
        new LogicalOperator(Token.LOGICAL_OR_OP) {
            
            @Override
            public TyBoolean operate(TyObject first, InstructionSet second, TyRuntime runtime) {
                
                if (TrinityNatives.toBoolean(first)) {
                    
                    return TyBoolean.TRUE;
                    
                } else {
                    
                    return TyBoolean.valueFor(TrinityNatives.toBoolean(second.evaluate(TyObject.NONE, runtime)));
                }
            }
        };
        
        // Binary operators
        // Order of operations is from bottom to top
        // Operators at the top are separated out first,
        // meaning that the ones at the bottom are executed first
        //
        // Ex. 10 + 10 * 10
        //  -> (10) + (10 * 10)
        //  -> (10) + ((10) * (10))
        
        BinaryOperator bitwiseOr = new BinaryOperator(Token.BITWISE_OR_OP) {
            
            @Override
            public TyObject operate(TyObject first, TyObject second, TyRuntime runtime) {
                
                return TrinityMath.bitwiseOr(first, second);
            }
        };
        BinaryOperator bitwiseXor = new BinaryOperator(Token.BITWISE_XOR_OP) {
            
            @Override
            public TyObject operate(TyObject first, TyObject second, TyRuntime runtime) {
                
                return TrinityMath.bitwiseXor(first, second);
            }
        };
        BinaryOperator bitwiseAnd = new BinaryOperator(Token.BITWISE_AND_OP) {
            
            @Override
            public TyObject operate(TyObject first, TyObject second, TyRuntime runtime) {
                
                return TrinityMath.bitwiseAnd(first, second);
            }
        };
        
        BinaryOperator equalTo = new BinaryOperator(Token.EQUAL_TO_OP) {
            
            @Override
            public TyObject operate(TyObject first, TyObject second, TyRuntime runtime) {
                
                return first.tyInvoke("==", runtime, null, null, second);
            }
        };
        new BinaryOperator(Token.NOT_EQUAL_TO_OP) {
            
            @Override
            public TyObject operate(TyObject first, TyObject second, TyRuntime runtime) {
                
                return TyBoolean.valueFor(!TrinityNatives.toBoolean(first.tyInvoke("==", runtime, null, null, second)));
            }
        };
        new BinaryOperator(Token.LESS_THAN_OP) {
            
            @Override
            public TyObject operate(TyObject first, TyObject second, TyRuntime runtime) {
                
                int comparisonInt = TrinityNatives.toInt(first.tyInvoke("<=>", runtime, null, null, second));
                
                return comparisonInt < 0 ? TyBoolean.TRUE : TyBoolean.FALSE;
            }
        };
        new BinaryOperator(Token.LESS_THAN_OR_EQUAL_TO_OP) {
            
            @Override
            public TyObject operate(TyObject first, TyObject second, TyRuntime runtime) {
                
                int comparisonInt = TrinityNatives.toInt(first.tyInvoke("<=>", runtime, null, null, second));
                
                return comparisonInt <= 0 ? TyBoolean.TRUE : TyBoolean.FALSE;
            }
        };
        new BinaryOperator(Token.GREATER_THAN_OP) {
            
            @Override
            public TyObject operate(TyObject first, TyObject second, TyRuntime runtime) {
                
                int comparisonInt = TrinityNatives.toInt(first.tyInvoke("<=>", runtime, null, null, second));
                
                return comparisonInt > 0 ? TyBoolean.TRUE : TyBoolean.FALSE;
            }
        };
        new BinaryOperator(Token.GREATER_THAN_OR_EQUAL_TO_OP) {
            
            @Override
            public TyObject operate(TyObject first, TyObject second, TyRuntime runtime) {
                
                int comparisonInt = TrinityNatives.toInt(first.tyInvoke("<=>", runtime, null, null, second));
                
                return comparisonInt >= 0 ? TyBoolean.TRUE : TyBoolean.FALSE;
            }
        };
        BinaryOperator comparison = new BinaryOperator(Token.COMPARISON_OP) {
            
            @Override
            public TyObject operate(TyObject first, TyObject second, TyRuntime runtime) {
                
                return first.tyInvoke("<=>", runtime, null, null, second);
            }
        };
        
        BinaryOperator leftShift = new BinaryOperator(Token.LEFT_SHIFT_OP) {
            
            @Override
            public TyObject operate(TyObject first, TyObject second, TyRuntime runtime) {
                
                return first.tyInvoke("<<", runtime, null, null, second);
            }
        };
        BinaryOperator rightShift = new BinaryOperator(Token.RIGHT_SHIFT_OP) {
            
            @Override
            public TyObject operate(TyObject first, TyObject second, TyRuntime runtime) {
                
                return first.tyInvoke(">>", runtime, null, null, second);
            }
        };
        BinaryOperator rightLogicalShift = new BinaryOperator(Token.RIGHT_LOGICAL_SHIFT_OP) {
            
            @Override
            public TyObject operate(TyObject first, TyObject second, TyRuntime runtime) {
                
                return first.tyInvoke(">>>", runtime, null, null, second);
            }
        };
        
        BinaryOperator minus = new BinaryOperator(Token.MINUS_OP) {
            
            @Override
            public TyObject operate(TyObject first, TyObject second, TyRuntime runtime) {
                
                return first.tyInvoke("-", runtime, null, null, second);
            }
        };
        BinaryOperator plus = new BinaryOperator(Token.PLUS_OP) {
            
            @Override
            public TyObject operate(TyObject first, TyObject second, TyRuntime runtime) {
                
                TyObject trueFirst = first;
                if (TrinityNatives.isInstanceOf(second, TrinityNatives.Classes.STRING) && !TrinityNatives.isInstanceOf(first, TrinityNatives.Classes.STRING)) {
                    
                    trueFirst = first.tyInvoke("toString", runtime, null, null);
                }
                
                return trueFirst.tyInvoke("+", runtime, null, null, second);
            }
        };
        BinaryOperator modulus = new BinaryOperator(Token.MODULUS_OP) {
            
            @Override
            public TyObject operate(TyObject first, TyObject second, TyRuntime runtime) {
                
                return first.tyInvoke("%", runtime, null, null, second);
            }
        };
        BinaryOperator divide = new BinaryOperator(Token.DIVIDE_OP) {
            
            @Override
            public TyObject operate(TyObject first, TyObject second, TyRuntime runtime) {
                
                return first.tyInvoke("/", runtime, null, null, second);
            }
        };
        BinaryOperator multiply = new BinaryOperator(Token.MULTIPLY_OP) {
            
            @Override
            public TyObject operate(TyObject first, TyObject second, TyRuntime runtime) {
                
                if (TrinityNatives.isInstanceOf(second, TrinityNatives.Classes.STRING)) {
                    
                    return second.tyInvoke("*", runtime, null, null, first);
                    
                } else {
                    
                    return first.tyInvoke("*", runtime, null, null, second);
                }
            }
        };
        
        // Register operators that function via methods
        Operators.registerOperatorMethod(equalTo);
        Operators.registerOperatorMethod(comparison);
        Operators.registerOperatorMethod(plus);
        Operators.registerOperatorMethod(minus);
        Operators.registerOperatorMethod(multiply);
        Operators.registerOperatorMethod(divide);
        Operators.registerOperatorMethod(modulus);
        Operators.registerOperatorMethod(leftShift);
        Operators.registerOperatorMethod(rightShift);
        Operators.registerOperatorMethod(rightLogicalShift);
        
        // Assignment Operators
        AssignmentOperators.registerAssignmentOperator(Token.ASSIGNMENT_OP, null);
        AssignmentOperators.registerAssignmentOperator(Token.NIL_ASSIGNMENT_OP, null);
        AssignmentOperators.registerAssignmentOperator(Token.PLUS_EQUAL_OP, plus);
        AssignmentOperators.registerAssignmentOperator(Token.MINUS_EQUAL_OP, minus);
        AssignmentOperators.registerAssignmentOperator(Token.MULTIPLY_EQUAL_OP, multiply);
        AssignmentOperators.registerAssignmentOperator(Token.DIVIDE_EQUAL_OP, divide);
        AssignmentOperators.registerAssignmentOperator(Token.MODULUS_EQUAL_OP, modulus);
        AssignmentOperators.registerAssignmentOperator(Token.BITWISE_OR_EQUAL_OP, bitwiseOr);
        AssignmentOperators.registerAssignmentOperator(Token.BITWISE_XOR_EQUAL_OP, bitwiseXor);
        AssignmentOperators.registerAssignmentOperator(Token.BITWISE_AND_EQUAL_OP, bitwiseAnd);
        AssignmentOperators.registerAssignmentOperator(Token.LEFT_SHIFT_EQUAL_OP, leftShift);
        AssignmentOperators.registerAssignmentOperator(Token.RIGHT_SHIFT_EQUAL_OP, rightShift);
        AssignmentOperators.registerAssignmentOperator(Token.RIGHT_LOGICAL_SHIFT_EQUAL_OP, rightLogicalShift);
        
        // Unary operators
        new ArithmeticUnaryOperator(Token.PLUS_OP) {
            
            @Override
            public TyObject operate(TyObject value, TyRuntime runtime) {
                
                // Values are positive by default
                //
                // + unary operator may be used for clarity
                // but has no effect on values
                
                return value;
            }
        };
        new ArithmeticUnaryOperator(Token.MINUS_OP) {
            
            @Override
            public TyObject operate(TyObject value, TyRuntime runtime) {
                
                return value.tyInvoke("*", runtime, null, null, TyInt.NEGATIVE_ONE);
            }
        };
        new ArithmeticUnaryOperator(Token.BITWISE_COMPLEMENT_OP) {
            
            @Override
            public TyObject operate(TyObject value, TyRuntime runtime) {
                
                return TrinityMath.bitwiseComplement(value);
            }
        };
        new UnaryOperator(Token.LOGICAL_NEGATION_OP) {
            
            @Override
            public TyObject operate(TyObject value, TyRuntime runtime) {
                
                return TyBoolean.valueFor(!TrinityNatives.toBoolean(value));
            }
        };
    }
}
