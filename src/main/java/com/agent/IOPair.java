package com.agent;

import java.util.ArrayList;

public class IOPair {

    public ArrayList<String> inputs;
    public ArrayList<String> outputs;

    public IOPair() {
        inputs = new ArrayList<String>();
        outputs = new ArrayList<String>();
    }

    public IOPair(ArrayList<String> i, ArrayList<String> o) {
        inputs = i;
        outputs = o;
    }
}
