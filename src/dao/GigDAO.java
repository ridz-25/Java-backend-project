package src.dao;

import java.util.List;
import java.util.Optional;

import src.model.Gig;

public interface GigDAO {

    boolean saveGig(Gig gig);

    Optional<Gig> getGigById(int gigId);

    List<Gig> getAllGigs();

    List<Gig> getGigsBySeller(int sellerId);

    List<Gig> getGigsByCategory(int categoryId);

    List<Gig> searchGigs(String keyword);

    boolean updateGig(Gig gig);

    boolean deleteGig(int gigId);
}