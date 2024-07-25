package pwav2.com.pw.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pwav2.com.pw.domain.Sandalia;
import pwav2.com.pw.repository.SandaliaRepository;

import java.util.List;
import java.util.Optional;

@Service
public class SandaliaService {

    private final SandaliaRepository repository;

    @Autowired
    public SandaliaService(SandaliaRepository repository) {
        this.repository = repository;
    }

    public Optional<Sandalia> findById(String id) {
        return repository.findById(id);
    }

    public void delete(String id) {
        repository.deleteById(id);
    }

    public Sandalia update(Sandalia sandalia) {
        sandalia.regrasDeNegocioParaCadastro();
        return repository.saveAndFlush(sandalia);
    }

    public Sandalia create(Sandalia sandalia) {
        sandalia.regrasDeNegocioParaCadastro();
        return repository.save(sandalia);
    }

    public List<Sandalia> findAll() {
        return repository.findAll();
    }
}
