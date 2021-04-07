package com.huafagroup.activiti.service;

import com.huafagroup.activiti.domain.dto.ActivitiHighLineDTO;

public interface IActivitiHistoryService {
    public ActivitiHighLineDTO gethighLine(String instanceId);
}
