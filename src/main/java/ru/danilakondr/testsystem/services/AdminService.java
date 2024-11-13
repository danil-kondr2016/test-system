package ru.danilakondr.testsystem.services;

import ru.danilakondr.testsystem.info.SystemInfo;
import ru.danilakondr.testsystem.data.UserSession;

public interface AdminService extends UserService {
    SystemInfo getSystemInfo(UserSession session);
}
