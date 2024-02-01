package everyide.webide.terminal.domain;

import everyide.webide.BaseEntity;
import everyide.webide.container.domain.Container;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "terminals")
public class Terminal extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private boolean activeStatus;

    @OneToOne(mappedBy = "terminal")
    private Container container;
}
