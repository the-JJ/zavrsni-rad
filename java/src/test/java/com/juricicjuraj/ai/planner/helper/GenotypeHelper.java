package com.juricicjuraj.ai.planner.helper;

import com.juricicjuraj.ai.fasttime.Duration;
import com.juricicjuraj.ai.planner.evolution.MomentSeqFactory;
import com.juricicjuraj.ai.planner.model.Task;
import com.juricicjuraj.ai.planner.model.evolution.EmptyMoment;
import com.juricicjuraj.ai.planner.model.evolution.Moment;
import com.juricicjuraj.ai.planner.model.evolution.TaskMoment;
import org.jenetics.EnumGene;
import org.jenetics.Genotype;
import org.jenetics.PermutationChromosome;
import org.jenetics.util.ISeq;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GenotypeHelper {
    private static final LocalDateTime SOMETIME = LocalDateTime.parse("2017-01-12T00:00");

    public static Genotype<EnumGene<Moment>> createGenotype(List<Moment> moments) {
        ISeq<Moment> momentsISeq = ISeq.of(moments);

        List<EnumGene<Moment>> genes = new ArrayList<>();
        int size = moments.size();
        for (int i = 0; i < size; i++) {
            genes.add(EnumGene.of(i, momentsISeq));
        }

        ISeq<EnumGene<Moment>> alleles = ISeq.of(genes);

        PermutationChromosome<Moment> chromosome = new PermutationChromosome<>(alleles);
        return Genotype.of(chromosome);
    }

    /**
     * Creates a list of moments from given string representation:
     * [1|1|2|some task|2|1|0|0]
     * @param string
     * @return
     */
    public static List<Moment> createMoments(String string, MomentSeqFactory msf) {
        int lastIndex = string.length()-1;
        if (string.charAt(0) != '[' || string.charAt(lastIndex) != ']') {
            throw new IllegalArgumentException("Illegal format. Expected [ and ]");
        }
        String[] momentsString = string.substring(1, lastIndex).split("\\|");

        Map<String, Task> taskMap = new HashMap<>();
        Map<Task, Integer> taskIdMap = new HashMap<>();

        List<Moment> moments = new ArrayList<>();
        for(String name : momentsString) {
            if (name.equals("0")) {
                moments.add(EmptyMoment.create(msf));
                continue;
            }

            Task task = taskMap.computeIfAbsent(name, k -> new Task(name, Duration.ZERO, SOMETIME));
            Integer id = taskIdMap.merge(task, 0, (a,b) -> b+1);

            moments.add(new TaskMoment(task, id, msf));
        }

        return moments;
    }
}
