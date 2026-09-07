package com.ys.hsm.society.service;

import com.ys.hsm.society.dto.request.SocietyRequest;
import com.ys.hsm.society.dto.response.SocietyResponse;

public interface SocietyService {

     SocietyResponse createSociety(SocietyRequest societyRequest);
     SocietyResponse approveSociety(String societyId);
     SocietyResponse rejectSociety(String societyId);
}
