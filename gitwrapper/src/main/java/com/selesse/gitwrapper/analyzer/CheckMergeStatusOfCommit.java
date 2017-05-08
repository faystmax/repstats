package com.selesse.gitwrapper.analyzer;

import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA
 * User: Maxim Amosov <faystmax@gmail.com>
 * Date: 06.05.2017
 * Time: 22:25
 */
public class CheckMergeStatusOfCommit {

    private Repository repository;

    CheckMergeStatusOfCommit(Repository repository) {
        this.repository = repository;
    }

    public void CheckMergeStatusOfCommit() throws IOException {

        Ref head = repository.getRef("refs/heads/master");

        // a RevWalk allows to walk over commits based on some filtering that is defined
        RevWalk walk = new RevWalk(repository);
        RevCommit commit = walk.parseCommit(head.getObjectId());
        System.out.println("Start-Commit: " + commit);

        System.out.println("Walking all commits starting at HEAD");
        walk.markStart(commit);
        int count = 0;
        for (RevCommit rev : walk) {
            System.out.println("Commit: " + rev);
            count++;
        }
        System.out.println(count);

        walk.dispose();


        RevWalk revWalk = new RevWalk(repository);
        RevCommit masterHead = revWalk.parseCommit(repository.resolve("refs/heads/master"));

        // first a commit that was merged
        ObjectId id = repository.resolve("05d18a76875716fbdbd2c200091b40caa06c713d");
        System.out.println("Had id: " + id);
        RevCommit otherHead = revWalk.parseCommit(id);

        if (revWalk.isMergedInto(otherHead, masterHead)) {
            System.out.println("Commit " + otherHead + " is merged into master");
        } else {
            System.out.println("Commit " + otherHead + " is NOT merged into master");
        }


        // then a commit on a test-branch which is not merged
        id = repository.resolve("ae70dd60a7423eb07893d833600f096617f450d2");
        System.out.println("Had id: " + id);
        otherHead = revWalk.parseCommit(id);

        if (revWalk.isMergedInto(otherHead, masterHead)) {
            System.out.println("Commit " + otherHead + " is merged into master");
        } else {
            System.out.println("Commit " + otherHead + " is NOT merged into master");
        }

        // and finally master-HEAD itself
        id = repository.resolve("HEAD");
        System.out.println("Had id: " + id);
        otherHead = revWalk.parseCommit(id);

        if (revWalk.isMergedInto(otherHead, masterHead)) {
            System.out.println("Commit " + otherHead + " is merged into master");
        } else {
            System.out.println("Commit " + otherHead + " is NOT merged into master");
        }

        revWalk.dispose();


    }
}
