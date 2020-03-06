package mops.database.dto;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import lombok.Data;

@Data
@Table("einheit")
public class EinheitDto {
  @Id
  Long id;
  String name;
}
