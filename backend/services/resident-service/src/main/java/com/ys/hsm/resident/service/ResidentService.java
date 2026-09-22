package com.ys.hsm.resident.service;

import com.ys.hsm.resident.dto.request.ResidentRequest;
import com.ys.hsm.resident.entity.Resident;

public interface ResidentService {
    Resident registerResident(ResidentRequest request, String authorizationHeader);
    Resident getResidentById(String residentId);
}
