package com.agent;

import java.util.HashMap;

public class ReportTable {
    private HashMap<String, ExecHashMap> table;
    private int max_outputs_for_inputs;

    public ReportTable() {
        table = new HashMap<>();
        max_outputs_for_inputs = 5;
    }

    public ReportTable(int cap) {
        table = new HashMap<>();
        max_outputs_for_inputs = cap;
    }

    /**
     * Report the inputs and outputs of a function execution to report_table
     * 
     * @param func_name name of the function being reported
     * @param io        a pair of inputs and outputs of the function execution
     */
    public void report(final String func_name, IOPair io) {
        if (table.get(func_name) == null) {
            ExecHashMap map = new ExecHashMap(max_outputs_for_inputs);
            map.insert(io);
            table.put(func_name, map);
        } else {
            table.get(func_name).insert(io);
        }
    }

    public String toString() {
        return table.toString();
    }
}
