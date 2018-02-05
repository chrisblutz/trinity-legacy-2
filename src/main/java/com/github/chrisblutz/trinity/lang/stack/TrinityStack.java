package com.github.chrisblutz.trinity.lang.stack;

import java.util.*;


/**
 * @author Christopher Lutz
 */
public class TrinityStack {
    
    private static Map<Thread, TrinityStack> threadStacks = new HashMap<>();
    
    private List<StackFrame> stack = new ArrayList<>();
    
    public TrinityStack(TrinityStack parent) {
        
        if (parent != null) {
            
            Collections.addAll(stack, parent.getStack());
        }
    }
    
    public void add(String fileName, int lineNumber, String usable, String method) {
        
        stack.add(0, new StackFrame(fileName, lineNumber, usable, method));
    }
    
    public void pop() {
        
        if (size() > 0) {
            
            stack.remove(0);
        }
    }
    
    public int size() {
        
        return stack.size();
    }
    
    public void popToSize(int size) {
        
        while (size() > size) {
            
            pop();
        }
    }
    
    public StackFrame[] getStack() {
        
        return stack.toArray(new StackFrame[stack.size()]);
    }
    
    public StackFrame get(int index) {
        
        return stack.get(index);
    }
    
    public void clear() {
        
        stack.clear();
    }
    
    public static TrinityStack getCurrentThreadStack() {
        
        return getThreadStack(Thread.currentThread());
    }
    
    public static TrinityStack getThreadStack(Thread thread) {
        
        if (!threadStacks.containsKey(thread)) {
            
            threadStacks.put(thread, new TrinityStack(null));
        }
        
        return threadStacks.get(thread);
    }
    
    public static void clearStacks() {
        
        for (Thread thread : threadStacks.keySet()) {
            
            threadStacks.get(thread).clear();
        }
        
        threadStacks.clear();
    }
}
