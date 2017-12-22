package com.github.chrisblutz.trinity.lang.procedures;

import com.github.chrisblutz.trinity.lang.TyObject;
import com.github.chrisblutz.trinity.lang.TyRuntime;
import com.github.chrisblutz.trinity.lang.errors.Errors;
import com.github.chrisblutz.trinity.lang.types.TyArray;
import com.github.chrisblutz.trinity.lang.types.TyProcedureObject;
import com.github.chrisblutz.trinity.utils.ArrayUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * @author Christopher Lutz
 */
public class TyProcedure {
    
    private ProcedureAction action;
    private List<String> mandatoryParameters = new ArrayList<>();
    private Map<String, ProcedureAction> optionalParameters = new LinkedHashMap<>();
    private String blockParameter = null, overflowParameter = null;
    private boolean rigid = true;
    
    public TyProcedure(ProcedureAction action, boolean rigid) {
        
        this(action, new ArrayList<>(), new LinkedHashMap<>(), null, null, rigid);
    }
    
    public TyProcedure(ProcedureAction action, List<String> mandatoryParameters, Map<String, ProcedureAction> optionalParameters, String blockParameter, boolean rigid) {
        
        this(action, mandatoryParameters, optionalParameters, blockParameter, null, rigid);
    }
    
    public TyProcedure(ProcedureAction action, List<String> mandatoryParameters, Map<String, ProcedureAction> optionalParameters, String blockParameter, String overflowParameter, boolean rigid) {
        
        this.action = action;
        this.mandatoryParameters = mandatoryParameters;
        this.optionalParameters = optionalParameters;
        this.blockParameter = blockParameter;
        this.overflowParameter = overflowParameter;
        this.rigid = rigid;
    }
    
    public ProcedureAction getAction() {
        
        return action;
    }
    
    public List<String> getMandatoryParameters() {
        
        return mandatoryParameters;
    }
    
    public Map<String, ProcedureAction> getOptionalParameters() {
        
        return optionalParameters;
    }
    
    public String getBlockParameter() {
        
        return blockParameter;
    }
    
    public void setBlockParameter(String blockParameter) {
        
        this.blockParameter = blockParameter;
    }
    
    public String getOverflowParameter() {
        
        return overflowParameter;
    }
    
    public void setOverflowParameter(String overflowParameter) {
        
        this.overflowParameter = overflowParameter;
    }
    
    public boolean isRigid() {
        
        return rigid;
    }
    
    public TyObject call(TyRuntime runtime, TyProcedure subProcedure, TyRuntime subProcedureRuntime, TyObject thisObj, TyObject... args) {
        
        for (String opt : getOptionalParameters().keySet()) {
            
            ProcedureAction action = getOptionalParameters().get(opt);
            runtime.setVariable(opt, action.onAction(runtime, TyObject.NONE));
        }
        
        boolean blockFlag = true;
        if (getBlockParameter() != null) {
            
            TyObject obj = null;
            if (subProcedure != null) {
                
                obj = new TyProcedureObject(subProcedure, subProcedureRuntime);
                blockFlag = false;
                
            } else if (!runtime.hasVariable(getBlockParameter())) {
                
                obj = new TyProcedureObject(new TyProcedure((runtime1, thisObj1, args1) -> TyObject.NONE, false), new TyRuntime());
            }
            
            if (obj != null) {
                
                runtime.setVariable(getBlockParameter(), obj);
            }
        }
        
        runtime.setProcedure(subProcedure);
        
        String[] varNames = new String[getMandatoryParameters().size() + getOptionalParameters().size()];
        int i;
        for (i = 0; i < getMandatoryParameters().size(); i++) {
            
            varNames[i] = getMandatoryParameters().get(i);
        }
        List<String> optionalParams = new ArrayList<>(getOptionalParameters().keySet());
        for (int j = 0; j < getOptionalParameters().size(); j++) {
            
            varNames[i++] = optionalParams.get(j);
        }
        optionalParams.clear();
        
        List<TyObject> overflow = new ArrayList<>();
        
        int nameIndex = 0;
        for (i = 0; i < args.length; i++) {
            
            TyObject arg = args[i];
            if (blockFlag && getBlockParameter() != null && arg instanceof TyProcedureObject) {
                
                runtime.setVariable(getBlockParameter(), arg);
                blockFlag = false;
                
            } else if (varNames.length > nameIndex) {
                
                runtime.setVariable(varNames[nameIndex++], arg);
                
            } else if (getOverflowParameter() != null && i == args.length - 1 && overflow.isEmpty() && arg instanceof TyArray && !ArrayUtils.isSolid((TyArray) arg, runtime)) {
                
                overflow.addAll(((TyArray) arg).getInternal());
                
            } else if (getOverflowParameter() != null) {
                
                overflow.add(arg);
                
            } else if (isRigid()) {
                
                throwArgumentNumberError(runtime);
            }
        }
        
        if (varNames.length - getOptionalParameters().size() > nameIndex) {
            
            throwArgumentNumberError(runtime);
        }
        
        if (getOverflowParameter() != null) {
            
            runtime.setVariable(getOverflowParameter(), new TyArray(overflow));
        }
        
        return getAction().onAction(runtime, thisObj, args);
    }
    
    private void throwArgumentNumberError(TyRuntime runtime) {
        
        Errors.throwError(Errors.Classes.ARGUMENT_ERROR, runtime, "Procedure takes " + getMandatoryParameters().size() + " argument(s).");
    }
}
