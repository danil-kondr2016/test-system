package ru.danilakondr.testsystem.services;

import ru.danilakondr.testsystem.info.SystemInfo;
import ru.danilakondr.testsystem.data.UserSession;

public interface AdminService {
    SystemInfo getSystemInfo(UserSession session);
}
