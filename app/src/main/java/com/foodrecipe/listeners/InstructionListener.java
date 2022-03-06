package com.foodrecipe.listeners;

import com.foodrecipe.models.InstructionsResponse;

import java.util.List;

public interface InstructionListener {
    void didFetch(List<InstructionsResponse> response, String message);
    void didError(String message);

}
