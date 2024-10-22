package com.petadoption.server;

import io.grpc.stub.StreamObserver;  // Import gRPC's StreamObserver, used to handle asynchronous responses
import petadoption.PetAdoptionServiceGrpc;  // Import the gRPC service base class generated from the Proto file
import petadoption.PetAdoption.*;  // Import message classes generated from the Proto file (e.g., PetInfo, RegisterResponse, SearchRequest, etc.)

import java.util.ArrayList;  // Import ArrayList to store pet information
import java.util.List;  // Import List interface to define the pet database list

// PetAdoptionServiceImpl class extends PetAdoptionServiceGrpc.PetAdoptionServiceImplBase
// It implements the RegisterPet and SearchPet methods of the gRPC service
public class PetAdoptionServiceImpl extends PetAdoptionServiceGrpc.PetAdoptionServiceImplBase {

    // Define an in-memory database to store all registered pet information
    private List<PetInfo> petDatabase = new ArrayList<>();

    // Override the registerPet method to handle pet registration requests
    @Override
    public void registerPet(PetInfo request, StreamObserver<RegisterResponse> responseObserver) {
        // Add the pet information from the request to the in-memory database
        petDatabase.add(request);

        // Build the RegisterResponse response, returning a success message for registration
        RegisterResponse response = RegisterResponse.newBuilder()
                .setMessage("Pet registered successfully!")  // Set the response message
                .build();

        // Send the response to the client
        responseObserver.onNext(response);

        // Complete the request processing
        responseObserver.onCompleted();
    }

    // Override the searchPet method to handle pet search requests
    @Override
    public void searchPet(SearchRequest request, StreamObserver<SearchResponse> responseObserver) {
        // Create a list to store pets that match the search criteria
        List<PetInfo> matchingPets = new ArrayList<>();

        // Iterate over all registered pets to find those that match the search criteria
        for (PetInfo pet : petDatabase) {
            // Match pet information based on the key and value from the client request
            if ((request.getKey().equals("name") && pet.getName().equals(request.getValue())) ||
                    (request.getKey().equals("gender") && pet.getGender().equals(request.getValue())) ||
                    (request.getKey().equals("age") && pet.getAge() == Integer.parseInt(request.getValue())) ||
                    (request.getKey().equals("breed") && pet.getBreed().equals(request.getValue()))) {
                // If a match is found, add the pet to the results list
                matchingPets.add(pet);
            }
        }

        // Build the SearchResponse response, including all matching pets
        SearchResponse response = SearchResponse.newBuilder()
                .addAllPets(matchingPets)  // Add matching pets to the response
                .build();

        // Send the response to the client
        responseObserver.onNext(response);

        // Complete the request processing
        responseObserver.onCompleted();
    }
}
