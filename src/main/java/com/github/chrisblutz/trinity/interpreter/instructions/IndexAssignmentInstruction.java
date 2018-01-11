package com.github.chrisblutz.trinity.interpreter.instructions;

import com.github.chrisblutz.trinity.interpreter.AssignmentOperators;
import com.github.chrisblutz.trinity.interpreter.BinaryOperator;
import com.github.chrisblutz.trinity.interpreter.Location;
import com.github.chrisblutz.trinity.lang.TyObject;
import com.github.chrisblutz.trinity.lang.TyRuntime;
import com.github.chrisblutz.trinity.parser.tokens.Token;


/**
 * @author Christopher Lutz
 */
public class IndexAssignmentInstruction extends Instruction {
    
    private Token operator;
    private InstructionSet object, value;
    private InstructionSet[] indices;
    
    public IndexAssignmentInstruction(Token operator, InstructionSet object, InstructionSet[] indices, InstructionSet value, Location location) {
        
        super(location);
        
        this.operator = operator;
        this.object = object;
        this.indices = indices;
        this.value = value;
    }
    
    public Token getOperator() {
        
        return operator;
    }
    
    public InstructionSet getObject() {
        
        return object;
    }
    
    public InstructionSet[] getIndices() {
        
        return indices;
    }
    
    public InstructionSet getValue() {
        
        return value;
    }
    
    @Override
    protected TyObject evaluate(TyObject thisObj, TyRuntime runtime) {
        
        updateLocation(runtime);
        
        TyObject assignmentObject = getObject().evaluate(TyObject.NONE, runtime);
        TyObject object = getValue().evaluate(TyObject.NONE, runtime);
        
        TyObject[] indices = new TyObject[getIndices().length];
        for (int i = 0; i < getIndices().length; i++) {
            
            indices[i] = getIndices()[i].evaluate(TyObject.NONE, runtime);
        }
        
        TyObject[] params = new TyObject[indices.length + 1];
        System.arraycopy(indices, 0, params, 0, indices.length);
        
        if (getOperator() == Token.ASSIGNMENT_OP) {
            
            params[params.length - 1] = object;
            assignmentObject.tyInvoke("[]=", runtime, null, null, params);
            
        } else {
            
            TyObject currentObject = assignmentObject.tyInvoke("[]", runtime, null, null, indices);
            
            if (getOperator() == Token.NIL_ASSIGNMENT_OP && currentObject == TyObject.NIL) {
                
                params[params.length - 1] = object;
                assignmentObject.tyInvoke("[]=", runtime, null, null, params);
                
            } else {
                
                BinaryOperator operator = AssignmentOperators.getOperator(getOperator());
                object = operator.operate(currentObject, object, runtime);
                
                params[params.length - 1] = object;
                assignmentObject.tyInvoke("[]=", runtime, null, null, params);
            }
        }
        
        return object;
    }
}
