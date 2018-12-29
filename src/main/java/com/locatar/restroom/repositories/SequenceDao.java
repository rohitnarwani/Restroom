package com.locatar.restroom.repositories;

import com.locatar.restroom.exception.SequenceException;

public interface SequenceDao {
	long getNextSequenceId(String key) throws SequenceException;
}
