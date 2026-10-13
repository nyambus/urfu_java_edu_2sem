package ru.education.lab3.service;

import ru.education.lab3.model.Request;
import ru.education.lab3.model.Response;

public interface RequestService {
    Response process(Request request);
}
