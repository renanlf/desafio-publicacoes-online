package online.renanlf.sysproc.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import online.renanlf.sysproc.model.Defendant;
import online.renanlf.sysproc.repository.DefendantRepository;

@Service
public class DefendantService {

	@Autowired
	private DefendantRepository repo;

	public Defendant save(Defendant defendant) {
		return repo.save(defendant);
	}

	public List<Defendant> findAll() {
		return repo.findAll();
	}

	public Optional<Defendant> findByCpfCnpj(String cpfCnpj) {
		return repo.findByCpfCnpj(cpfCnpj);
	}

	@Transactional
	public void deleteByCpfCnpj(String cpfCnpj) {
		repo.deleteByCpfCnpj(cpfCnpj);
	}

}
