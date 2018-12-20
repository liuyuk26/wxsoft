package com.watts.sys.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.watts.sys.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

	public User findOne(Long id);

	public User save(User u);

	@Query("select t from User t where t.name like %:name%")
	public List<User> findUserByName(@Param("name") String name);

	@Query("select t from User t where t.id=:id")
	public User findUserByID(@Param("id") Integer id);

	@Query("select t from User t")
	public List<User> findUserAll();
}
