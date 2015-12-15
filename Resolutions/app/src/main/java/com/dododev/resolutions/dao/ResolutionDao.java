package com.dododev.resolutions.dao;

import com.dododev.resolutions.model.Resolution;

import java.util.List;

/**
 * Created by dodo on 2015-12-14.
 */
public interface ResolutionDao {
    List<Resolution> findAll();
    Resolution getById(Long id);
    Long save(Resolution resolution);
}
