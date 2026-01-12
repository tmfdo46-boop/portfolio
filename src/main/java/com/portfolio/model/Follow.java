package com.portfolio.model;

import javax.persistence.*;

@Entity
@Table(name = "TB_FOLLOW", uniqueConstraints = @UniqueConstraint(columnNames = {"FOLLOWER_ID", "FOLLOWING_ID"}))
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FOLLOW_SEQ_GENERATOR")
    @SequenceGenerator(name = "FOLLOW_SEQ_GENERATOR", sequenceName = "TB_FOLLOW_SEQ", allocationSize = 1)
    @Column(name = "FOLLOW_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "FOLLOWER_ID", nullable = false)
    private User follower;

    @ManyToOne
    @JoinColumn(name = "FOLLOWING_ID", nullable = false)
    private User following;

    public Follow() {}

    public Follow(User follower, User following) {
        this.follower = follower;
        this.following = following;
    }

    // getter / setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getFollower() { return follower; }
    public void setFollower(User follower) { this.follower = follower; }

    public User getFollowing() { return following; }
    public void setFollowing(User following) { this.following = following; }
}
