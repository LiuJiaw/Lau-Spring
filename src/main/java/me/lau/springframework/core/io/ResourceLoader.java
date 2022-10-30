package me.lau.springframework.core.io;

public interface ResourceLoader {

    Resource getResource(String location);
}
