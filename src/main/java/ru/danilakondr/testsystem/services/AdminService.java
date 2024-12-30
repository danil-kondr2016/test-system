package ru.danilakondr.testsystem.services;

import ru.danilakondr.testsystem.info.SystemInfo;

public interface AdminService {
    void register(String email, String password);
    SystemInfo getSystemInfo();
}
