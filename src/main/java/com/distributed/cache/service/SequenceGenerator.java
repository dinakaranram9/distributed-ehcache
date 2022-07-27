package com.distributed.cache.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.distributed.cache.entity.DatabaseSequence;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SequenceGenerator {
	@Autowired
	MongoOperations mongoOperations;

	public long generateSequence(String seqName) {
		// get sequence id
		Query query = new Query(Criteria.where("_id").is(seqName));
		// increase sequence id by 1
		Update update = new Update();
		update.inc("seq", 1);
		// return new increased id
		FindAndModifyOptions options = new FindAndModifyOptions();
		options.returnNew(true);
		// this is the magic happened.
		DatabaseSequence seqId = mongoOperations.findAndModify(query, update, options, DatabaseSequence.class);
		// if no id, throws SequenceException
		// optional, just a way to tell user when the sequence id is failed to generate.
		if (seqId == null) {
			log.error("Unable to get sequence id for key : " + seqName);
			DatabaseSequence ds = new DatabaseSequence();
			ds.setId(seqName);
			ds.setSeq(1);
			mongoOperations.save(ds);
			return 1;
		}

		return seqId.getSeq();
	}
}
