package com.github.chrisblutz.trinity.lang.natives;

import com.github.chrisblutz.trinity.lang.TyObject;
import com.github.chrisblutz.trinity.lang.TyRuntime;
import com.github.chrisblutz.trinity.lang.errors.Errors;
import com.github.chrisblutz.trinity.lang.procedures.ProcedureAction;
import com.github.chrisblutz.trinity.natives.TrinityNatives;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * @author Christopher Lutz
 */
public class OperationProcedureAction implements ProcedureAction {
    
    private List<String> prohibitedFloatOperators = new ArrayList<>(Arrays.asList("<<", ">>", ">>>"));
    private String operation;
    
    public OperationProcedureAction(String operation) {
        
        this.operation = operation;
    }
    
    public String getOperation() {
        
        return operation;
    }
    
    @Override
    public TyObject onAction(TyRuntime runtime, TyObject thisObj, TyObject... args) {
        
        TyObject other = runtime.getVariable("a");
        
        double thisDouble = TrinityNatives.asNumber(thisObj);
        double otherDouble = TrinityNatives.asNumber(other);
        
        // Perform check for unsupported operations on floating-point values
        if (prohibitedFloatOperators.contains(operation) && (thisDouble % 1 != 0 || otherDouble % 1 != 0)) {
            
            Errors.throwError(Errors.Classes.UNSUPPORTED_ERROR, runtime, "Operation " + operation + " not supported on non-integer values.");
        }
        
        return TrinityNatives.wrapNumber(performOperation(thisDouble, otherDouble));
    }
    
    private double performOperation(double thisDouble, double otherDouble) {
        
        switch (operation) {
            
            case "+":
                
                return thisDouble + otherDouble;
            
            case "-":
                
                return thisDouble - otherDouble;
            
            case "*":
                
                return thisDouble * otherDouble;
            
            case "/":
                
                // Check for / by 0
                checkDivision(otherDouble);
                
                return thisDouble / otherDouble;
            
            case "%":
                
                // Check for / by 0
                checkDivision(otherDouble);
                
                return thisDouble - (Math.floor(thisDouble / otherDouble) * otherDouble);
            
            case "<<":
                
                return (long) thisDouble << (long) otherDouble;
            
            case ">>":
                
                return (long) thisDouble >> (long) otherDouble;
            
            case ">>>":
                
                return (long) thisDouble >>> (long) otherDouble;
            
            default:
                
                Errors.throwError(Errors.Classes.UNSUPPORTED_ERROR, "Operation " + operation + " not supported by " + TrinityNatives.Classes.NUMERIC + ".");
                return 0;
        }
    }
    
    private void checkDivision(double denominator) {
        
        if (denominator == 0) {
            
            Errors.throwError(Errors.Classes.ARITHMETIC_ERROR, "/ by 0.");
        }
    }
}
