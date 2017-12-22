package com.github.chrisblutz.trinity.lang.stack;

import java.util.*;


/**
 * @author Christopher Lutz
 */
public class TrinityStack {
    
    private static Map<Thread, TrinityStack> threadStacks = new HashMap<>();
    
    private List<StackElement> stack = new ArrayList<>();
    
    public TrinityStack(TrinityStack parent) {
        
        if (parent != null) {
            
            Collections.addAll(stack, parent.getStack());
        }
    }
    
    public void add(String fileName, int lineNumber) {
        
        stack.add(0, new StackElement(fileName, lineNumber));
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
    
    public StackElement[] getStack() {
        
        return stack.toArray(new StackElement[stack.size()]);
    }
    
    public StackElement get(int index) {
        
        return stack.get(index);
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
}
