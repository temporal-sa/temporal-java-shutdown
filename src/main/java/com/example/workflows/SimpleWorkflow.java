package com.example.workflows;

import com.example.model.SimpleInput;
import com.example.model.SimpleOutput;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface SimpleWorkflow {

    @WorkflowMethod(name = "SimpleJava")
    SimpleOutput execute(SimpleInput input);
}
