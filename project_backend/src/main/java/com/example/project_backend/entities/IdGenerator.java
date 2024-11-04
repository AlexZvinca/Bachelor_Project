package com.example.project_backend.entities;

import jakarta.persistence.Query;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.engine.spi.SharedSessionContractImplementor;

import java.io.Serializable;

public class IdGenerator implements IdentifierGenerator {

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object obj) {
        User user = (User) obj;
        County countyCode = user.getCounty();

        // Query for the current count of users with the same county code
        String queryStr = "SELECT COUNT(u) FROM User u WHERE u.county = :countyId";
        Query query = session.createQuery(queryStr);
        query.setParameter("countyId", countyCode);
        Long count = (Long) query.getSingleResult();

        // Format the ID with the county code and incrementing number
        return countyCode + String.format("%03d", count + 1);
    }
}
