package com.github.chrisblutz.trinity.interpreter.instructions;

import com.github.chrisblutz.trinity.interpreter.AssignmentOperators;
import com.github.chrisblutz.trinity.interpreter.BinaryOperator;
import com.github.chrisblutz.trinity.interpreter.Location;
import com.github.chrisblutz.trinity.lang.TyObject;
import com.github.chrisblutz.trinity.lang.TyRuntime;
import com.github.chrisblutz.trinity.lang.variables.VariableLocation;
import com.github.chrisblutz.trinity.lang.variables.VariableManager;
import com.github.chrisblutz.trinity.parser.tokens.Token;


/**
 * @author Christopher Lutz
 */
public class AssignmentInstruction extends Instruction {
    
    private Token operator;
    private InstructionSet remainder, value;
    private VariableLocationRetriever retriever;
    
    public AssignmentInstruction(Token operator, InstructionSet remainder, VariableLocationRetriever retriever, InstructionSet value, Location location) {
        
        super(location);
        
        this.operator = operator;
        this.remainder = remainder;
        this.retriever = retriever;
        this.value = value;
    }
    
    public Token getOperator() {
        
        return operator;
    }
    
    public InstructionSet getRemainder() {
        
        return remainder;
    }
    
    public VariableLocationRetriever getRetriever() {
        
        return retriever;
    }
    
    public InstructionSet getValue() {
        
        return value;
    }
    
    @Override
    protected TyObject evaluate(TyObject thisObj, TyRuntime runtime) {
        
        updateLocation(runtime);
        
        TyObject newThis = getRemainder().evaluate(TyObject.NONE, runtime);
        VariableLocation location = getRetriever().evaluate(newThis, runtime);
        
        if (location.checkScope(runtime)) {
            
            TyObject object = getValue().evaluate(TyObject.NONE, runtime);
            
            if (getOperator() == Token.ASSIGNMENT_OP) {
                
                VariableManager.put(location, object);
                
            } else {
                
                TyObject currentObject = location.getValue();
                
                if (getOperator() == Token.NIL_ASSIGNMENT_OP && currentObject == TyObject.NIL) {
                    
                    VariableManager.put(location, object);
                    
                } else {
                    
                    BinaryOperator operator = AssignmentOperators.getOperator(getOperator());
                    object = operator.operate(currentObject, object, runtime);
                    
                    VariableManager.put(location, object);
                }
            }
            
            return object;
            
        } else {
            
            location.getScope().reportAssignmentViolation(runtime);
            return TyObject.NIL;
        }
    }
}
