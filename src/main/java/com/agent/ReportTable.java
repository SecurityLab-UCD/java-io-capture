package com.agent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONObject;
// import com.google.gson.Gson;

public class ReportTable {
    private ConcurrentHashMap<String, ExecHashMap> table;
    // private HashMap<String, ExecHashMap> table;
    private int max_outputs_for_inputs;

    public ReportTable() {
        // table = new HashMap<>();
        table = new ConcurrentHashMap<>();
        max_outputs_for_inputs = 5;
    }

    public ReportTable(int cap) {
        // table = new HashMap<>();
        table = new ConcurrentHashMap<>();
        max_outputs_for_inputs = cap;
    }

    /**
     * Report the inputs and outputs of a function execution to report_table
     * 
     * @param func_name name of the function being reported
     * @param io        a pair of inputs and outputs of the function execution
     */
    public synchronized void report(final String func_name, IOPair io) {
        // if (table.get(func_name) == null) {
        if (table.get(func_name) == null) {
            ExecHashMap map = new ExecHashMap(max_outputs_for_inputs);
            map.insert(io);
            // table.put(func_name, map);
            table.put(func_name, map);
        } else {
            // table.get(func_name).insert(io);
            table.get(func_name).insert(io);
        }
    }

    public String toString() {
        return table.toString();
    }

    // Using JSONObject
    public ArrayList<JSONObject> toJson() {
        ArrayList<JSONObject> json_list = new ArrayList<>();
        for (String file_and_func_name : table.keySet()) {
            JSONObject json_object = new JSONObject();
            ExecHashMap exec_hash_map = table.get(file_and_func_name);
            json_object.put(file_and_func_name, exec_hash_map.toJsonValue());
            json_list.add(json_object);
        }
        return json_list;
    }
}

