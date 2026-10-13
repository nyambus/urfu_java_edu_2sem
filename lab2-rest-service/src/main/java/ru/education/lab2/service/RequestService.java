package ru.education.lab2.service;

import ru.education.lab2.model.Request;
import ru.education.lab2.model.Response;

public interface RequestService {
    Response process(Request request);
}
