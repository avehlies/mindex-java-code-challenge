package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.service.CompensationService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompensationServiceImpl implements CompensationService {
    private final CompensationRepository compensationRepository;

    public CompensationServiceImpl(final CompensationRepository compensationRepository) {
        this.compensationRepository = compensationRepository;
    }

    @Override
    public Compensation create(Compensation compensation) {
        return compensationRepository.insert(compensation);
    }

    @Override
    public List<Compensation> getByEmployeeId(String id) {
        return compensationRepository.findByEmployeeId(id);
    }
}
