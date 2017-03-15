package urteam.sport;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SportRepository extends JpaRepository<Sport, Long>{
	Sport findByName(String name);
}
