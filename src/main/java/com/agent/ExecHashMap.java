package com.agent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import java.util.concurrent.ConcurrentHashMap;

public class ExecHashMap {

    // works as `typedef`, which is not in Java
    public static class IOVectorSet extends HashSet<ArrayList<String>> {
    }

    // private HashMap<ArrayList<String>, IOVectorSet> map;
    private ConcurrentHashMap<ArrayList<String>, IOVectorSet> map;

    private int value_capacity;

    public ExecHashMap() {
        // map = new HashMap<ArrayList<String>, IOVectorSet>();
        map = new ConcurrentHashMap<ArrayList<String>, IOVectorSet>();
        value_capacity = 0;
    }

    public ExecHashMap(int cap) {
        // map = new HashMap<ArrayList<String>, IOVectorSet>();
        map = new ConcurrentHashMap<ArrayList<String>, IOVectorSet>();
        value_capacity = cap;
    }

    // Copy constructor
    public ExecHashMap(ExecHashMap exec_hash_map) {
        this.map = new ConcurrentHashMap<ArrayList<String>, IOVectorSet>(exec_hash_map.map);
        this.value_capacity = exec_hash_map.value_capacity;
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

    // public JSONObject toJson() {
    //     JSONObject json_object = new JSONObject();
    //     for (ArrayList<String> key : map.keySet()) {
    //         String input = Arrays.deepToString(key.toArray());
    //         // IOVectorSet outputs = map.get(key);
    //         // json_object.put(input, "new JSONArray(outputs)");
    //         json_object.put("test", "test");
    //     }
    //     System.out.println(json_object.toString());
    //     return json_object;
    // }

    // Try returning the ExecHashMap in list form to be serialized in ReportTable
    public ArrayList<ArrayList<Object>> toJsonValue() {
        ArrayList<ArrayList<Object>> io_list = new ArrayList<>();
        for (ArrayList<String> input : map.keySet()) {
            IOVectorSet outputs = map.get(input);
            // Create a list for each element in the overall list
            ArrayList<Object> element = new ArrayList<>();
            element.add(input);
            element.add(new ArrayList<ArrayList<String>>(outputs));
            io_list.add(element);
        }
        return io_list;
    }
}

