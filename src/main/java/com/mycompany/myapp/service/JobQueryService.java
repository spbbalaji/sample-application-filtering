package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.Job;
import com.mycompany.myapp.repository.JobRepository;
import com.mycompany.myapp.service.criteria.JobCriteria;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Job} entities in the database.
 * The main input is a {@link JobCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Job} or a {@link Page} of {@link Job} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class JobQueryService extends QueryService<Job> {

    private final Logger log = LoggerFactory.getLogger(JobQueryService.class);

    private final JobRepository jobRepository;

    public JobQueryService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    /**
     * Return a {@link List} of {@link Job} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Job> findByCriteria(JobCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Job> specification = createSpecification(criteria);
        return jobRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Job} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Job> findByCriteria(JobCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Job> specification = createSpecification(criteria);
        return jobRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(JobCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Job> specification = createSpecification(criteria);
        return jobRepository.count(specification);
    }

    /**
     * Function to convert {@link JobCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Job> createSpecification(JobCriteria criteria) {
        Specification<Job> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Job_.id));
            }
            if (criteria.getJobTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getJobTitle(), Job_.jobTitle));
            }
            if (criteria.getMinSalary() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMinSalary(), Job_.minSalary));
            }
            if (criteria.getMaxSalary() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMaxSalary(), Job_.maxSalary));
            }
            if (criteria.getTaskId() != null) {
                specification =
                    specification.and(buildSpecification(criteria.getTaskId(), root -> root.join(Job_.tasks, JoinType.LEFT).get(Task_.id)));
            }
            if (criteria.getEmployeeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getEmployeeId(), root -> root.join(Job_.employee, JoinType.LEFT).get(Employee_.id))
                    );
            }
        }
        return specification;
    }
}
