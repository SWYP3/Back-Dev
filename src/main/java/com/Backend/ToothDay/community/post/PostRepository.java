package com.Backend.ToothDay.community.post;

import com.Backend.ToothDay.community.post.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
public class PostRepository {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private PostJPARepository postJPARepository;

    public void save(Post post, List<Integer> keywordIds) {
        em.persist(post);
        if(CollectionUtils.isEmpty(keywordIds)) {
            Keyword keyword = em.find(Keyword.class,1);
            if (keyword == null) {
                throw new NullPointerException("Keyword with ID 1 is null");
            }
            PostKeyword postKeyword = new PostKeyword();
            PostKeywordId postKeywordId = new PostKeywordId(post.getId(), keyword.getId());
            postKeyword.setPostKeywordId(postKeywordId);
            postKeyword.setPost(post);
            postKeyword.setKeyword(keyword);
            em.persist(postKeyword);
            //post.getPostKeywords().add(postKeyword);
        }
        else {
            Keyword keyword1 = em.find(Keyword.class,1);
            if (keyword1 == null) {
                throw new NullPointerException("Keyword with ID 1 is null");
            }
            PostKeyword postKeyword1 = new PostKeyword();
            PostKeywordId postKeywordId1 = new PostKeywordId(post.getId(), keyword1.getId());
            postKeyword1.setPostKeywordId(postKeywordId1);
            postKeyword1.setPost(post);
            postKeyword1.setKeyword(keyword1);
            em.persist(postKeyword1);
            //post.getPostKeywords().add(postKeyword1);
            for (Integer keywordId : keywordIds) {
                Keyword keyword = em.find(Keyword.class, keywordId);
                if(keyword != null) {
                    PostKeyword postKeyword = new PostKeyword();
                    PostKeywordId postKeywordId = new PostKeywordId(post.getId(), keywordId);
                    postKeyword.setPostKeywordId(postKeywordId);
                    postKeyword.setPost(post);
                    postKeyword.setKeyword(keyword);
                    em.persist(postKeyword);
                    //post.getPostKeywords().add(postKeyword);
                }
                else {
                    throw new IllegalArgumentException("Keyword with ID " + keywordId + " not found");
                }
            }
        }
    }

    public void resave(Post post, List<Integer> keywordIds) {
        if(CollectionUtils.isEmpty(keywordIds)) {
            Keyword keyword = em.find(Keyword.class,1);
            if (keyword == null) {
                throw new NullPointerException("Keyword with ID 1 is null");
            }
            PostKeyword postKeyword = new PostKeyword();
            PostKeywordId postKeywordId = new PostKeywordId(post.getId(), keyword.getId());
            postKeyword.setPostKeywordId(postKeywordId);
            postKeyword.setPost(post);
            postKeyword.setKeyword(keyword);
            em.persist(postKeyword);
            //post.getPostKeywords().add(postKeyword);
        }
        else {
            Keyword keyword1 = em.find(Keyword.class,1);
            if (keyword1 == null) {
                throw new NullPointerException("Keyword with ID 1 is null");
            }
            PostKeyword postKeyword1 = new PostKeyword();
            PostKeywordId postKeywordId1 = new PostKeywordId(post.getId(), keyword1.getId());
            postKeyword1.setPostKeywordId(postKeywordId1);
            postKeyword1.setPost(post);
            postKeyword1.setKeyword(keyword1);
            em.persist(postKeyword1);
            //post.getPostKeywords().add(postKeyword1);
            for (Integer keywordId : keywordIds) {
                Keyword keyword = em.find(Keyword.class, keywordId);
                if(keyword != null) {
                    PostKeyword postKeyword = new PostKeyword();
                    PostKeywordId postKeywordId = new PostKeywordId(post.getId(), keywordId);
                    postKeyword.setPostKeywordId(postKeywordId);
                    postKeyword.setPost(post);
                    postKeyword.setKeyword(keyword);
                    em.persist(postKeyword);
                    //post.getPostKeywords().add(postKeyword);
                }
                else {
                    throw new IllegalArgumentException("Keyword with ID " + keywordId + " not found");
                }
            }
        }
    }

    public List<Post> findAllPaging(int limit, int offset) {
        return em.createQuery("from Post p ORDER BY p.createDate DESC", Post.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    public List<Post> findBySearchPaging(String search, int limit, int offset) {
        List<Long> postIds = postJPARepository.findPostIdsByTitleContaining(search);
        System.out.println(postIds);

        if (postIds.isEmpty()) {
            return new ArrayList<>();
        }

        // EntityManager로 offset, limit 적용하여 페이징
        TypedQuery<Post> query= em.createQuery(
                        "select p from Post p WHERE p.id IN :postIds ORDER BY p.createDate DESC", Post.class)
                .setParameter("postIds", postIds);
                        //.setFirstResult(offset)
                                //.setMaxResults(limit);

        //System.out.println(query.getSingleResult());

        return query.getResultList();
    }

    public List<Post> findByTitleContaining(String search, int limit, int offset) {
        // '%' + search + '%'로 검색 패턴 설정
        String searchPattern = "%" + search + "%";

        // JPQL 쿼리 작성
        TypedQuery<Post> query = em.createQuery(
                        "SELECT p FROM Post p WHERE p.title LIKE :searchPattern ORDER BY p.createDate DESC", Post.class)
                .setParameter("searchPattern", searchPattern);
                //.setFirstResult(offset)  // 페이징을 위한 offset 설정
                //.setMaxResults(limit);   // 페이징을 위한 limit 설정

        // 쿼리 실행 및 결과 반환
        return query.getResultList();
    }


    public List<Post> findByKeywordIdPaging(int keywordId, int limit, int offset) {
        //PostKeyword에서 keywordId로 Post 조회
        String queryStr = "SELECT pk.post FROM PostKeyword pk WHERE pk.keyword.id = :keywordId order by pk.post.createDate DESC";
        TypedQuery<Post> query = em.createQuery(queryStr, Post.class)
                .setParameter("keywordId", keywordId)
                .setFirstResult(offset)
                .setMaxResults(limit);

        return query.getResultList();
    }

    public Post findById(long postId) {
        return em.find(Post.class, postId);
    }

    public List<Post> findByUserIdPaging(long userId, int limit, int offset) {
        return em.createQuery("select p from Post p where p.user.id = :userId order by p.createDate DESC ",Post.class)
                .setParameter("userId",userId)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    public void delete(Post post) {
        em.remove(em.merge(post));
    }}
