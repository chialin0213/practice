package com.example.repository;

import com.example.entity.Titles;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.Predicate;
import java.util.List;

@Repository
public class TitlesRepository extends BaseJpaRepository<Titles,Long> {
    TitlesRepository(EntityManager em) {
        super(Titles.class, em);
    }

    /**
     * hql查询
     *
     * @param title
     * @return
     */
    public List<Titles> findByTitleNative(String title){
        String hql = "from Titles where title = :title";
        TypedQuery<Titles> query =em.createQuery(hql,Titles.class);
        query.setParameter("title",title);
        return query.getResultList();
    }

    /**
     * native query
     * @param title
     * @return
     */
    public List<Titles> findByTitle(String title){
        String sql = "select * from titles where title = :title";
        Query query = em.createNativeQuery(sql);
        query.setParameter("title",title);
        return query.getResultList();
    }

    /**
     * JPASpecification 實現複雜分頁查詢
     * @param titleDto
     * @param pageable
     * @return
     */
    public Page<Titles> findAllByPage(Titles titleDto, Pageable pageable) {
        Specification specification = (Specification) (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();
            if(!StringUtils.isEmpty(titleDto.getTitle())){
                predicate.getExpressions().add( criteriaBuilder.like(root.get("title"),titleDto.getTitle()));
            }
            if(titleDto.getFromDate() !=null){
                predicate.getExpressions().add(criteriaBuilder.greaterThanOrEqualTo(root.get("fromDate").as(String.class),titleDto.getFromDate()));
            }
            query.where(predicate);
            return predicate;
        };
        return  findAll(specification, pageable);
    }
}
