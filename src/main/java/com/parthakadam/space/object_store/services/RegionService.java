package com.parthakadam.space.object_store.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import jakarta.annotation.Generated;
import lombok.Getter;
import lombok.Setter;



@Service
public class  RegionService {

    private final Set<String> regions = new HashSet<>();

    public RegionService() {
        regions.add("localhost");
        // regions.add("india");
        // regions.add("us_south");
    }

    public boolean isValidRegion(String region) {
        System.out.println(regions);
        System.out.println(regions.contains(region.toLowerCase()));
        return regions.contains(region.toLowerCase());
    }
}