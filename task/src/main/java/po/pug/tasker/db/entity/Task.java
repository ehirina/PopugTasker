package po.pug.tasker.db.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "TASK")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ASSIGNEE_ID", referencedColumnName = "ID")
    private Account assignee;

    @Column(name = "STATUS")
    private Status status;

    public enum Status {
        ASSIGNED, DONE
    }
}
