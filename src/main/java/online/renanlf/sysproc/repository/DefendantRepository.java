package online.renanlf.sysproc.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import online.renanlf.sysproc.model.Defendant;

public interface DefendantRepository extends JpaRepository<Defendant, Long>{

	void deleteByCpfCnpj(String cpfCnpj);

	Optional<Defendant> findByCpfCnpj(String cpfCnpj);

}
