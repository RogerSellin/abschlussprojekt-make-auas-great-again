package mops.database;

import mops.database.dto.StudentDto;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentenJdbcRepository extends CrudRepository<StudentDto, Long> {
  //TODO
  @Query("")
  Long findId(String student);
}
