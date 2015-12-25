package com.dododev.resolutions.dao;

import com.dododev.resolutions.model.Resolution;
import com.dododev.resolutions.model.ResolutionStatusDict;

import java.util.List;

/**
 * Created by dodo on 2015-12-14.
 */
public interface ResolutionDao {
    List<Resolution> findAll();
    List<Resolution> findByType(ResolutionStatusDict status);
    Resolution getById(Long id);
    Long save(Resolution resolution);
    void delete(Resolution resolution);
}