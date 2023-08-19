package com.agent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class ExecHashMap {

    // works as `typedef`, which is not in Java
    public static class IOVectorSet extends HashSet<ArrayList<String>> {
    }

    private HashMap<ArrayList<String>, IOVectorSet> map;

    private int value_capacity;

    public ExecHashMap() {
        map = new HashMap<ArrayList<String>, IOVectorSet>();
        value_capacity = 0;
    }

    public ExecHashMap(int cap) {
        map = new HashMap<ArrayList<String>, IOVectorSet>();
        value_capacity = cap;
    }

    /**
     * Inset a pair of inputs and outputs to the hash map
     * 
     * @param io a pair of inputs (ArrayList) and outputs (ArrayList)
     */
    public void insert(IOPair io) {
        ArrayList<String> inputs = io.inputs;
        ArrayList<String> outputs = io.outputs;

        IOVectorSet outputSet = map.get(inputs);
        if (outputSet == null) {
            IOVectorSet newOutputSet = new IOVectorSet();
            newOutputSet.add(outputs);
            map.put(inputs, newOutputSet);
        } else if (map.get(inputs).size() < value_capacity) {
            map.get(inputs).add(outputs);
        }
    }

    /**
     * Get the list of outputs of a given inputs list
     * 
     * @param xs inputs
     * @return a set of outputs
     */
    public IOVectorSet get(final ArrayList<String> xs) {
        return map.get(xs);
    }

    public int size() {
        return map.size();
    }

    public String toString() {
        return map.toString();
    }
}
