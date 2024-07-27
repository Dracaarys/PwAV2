package pwav2.com.pw.domain;


import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity(name = "sandalia_tbl")
public class Sandalia {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotNull(message = "O campo 'isDeleted' não pode ser nulo.")
    private Boolean isDeleted;

    @NotBlank(message = "O campo 'imageUri' não pode conter caracteres em branco.")
    private String imageUri;

    @NotBlank(message = "O campo 'marca' não pode conter caracteres em branco.")
    @Size(min = 2, max = 50, message = "Houve um erro no cadastro do campo marca.")
    private String marca;

    @NotBlank(message = "O campo 'modelo' não pode conter caracteres em branco.")
    @Size(min = 2, max = 50, message = "Houve um erro no cadastro do campo modelo.")
    private String modelo;

    @NotNull(message = "O campo 'tamanho' não pode ser nulo.")
    @DecimalMin(value = "1", message = "O tamanho deve ser pelo menos 1.")
    private Integer tamanho;

    @NotNull(message = "O campo 'preco' não pode ser nulo.")
    @DecimalMin(value = "0.0", inclusive = false, message = "O preço deve ser maior que zero.")
    private Float preco;

    @NotBlank(message = "O campo 'cor' não pode conter caracteres em branco.")
    private String cor;

    private Long deletedAt;

    @Transient
    public String getPhotosImagePath() {
        if (imageUri == null || id == null) return null;

        return imageUri;

    }


    public void regrasDeNegocioParaCadastro(){
        marca = marca.toUpperCase();
        modelo = modelo.toUpperCase();
    }
}