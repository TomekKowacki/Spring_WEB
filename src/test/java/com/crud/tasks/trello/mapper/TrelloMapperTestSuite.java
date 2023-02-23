package com.crud.tasks.trello.mapper;

import com.crud.tasks.domain.*;
import com.crud.tasks.mapper.TrelloMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class TrelloMapperTestSuite {


    @Autowired
    private TrelloMapper trelloMapper;


    @Test
    public void testMapToBoards(){
        //Given
        List<TrelloListDto> trelloListDtos = List.of(new TrelloListDto("1", "Test", false));
        List<TrelloBoardDto> trelloBoardDtos = List.of(new TrelloBoardDto("Test1", "1", trelloListDtos));

        //When
        List<TrelloBoard> trelloBoards = trelloMapper.mapToBoards(trelloBoardDtos);

        //Then
        assertEquals("1", trelloBoards.get(0).getId());
        assertEquals("Test1", trelloBoards.get(0).getName());
        assertEquals(1, trelloListDtos.size());
    }

    @Test
    public void testMapToBoardDto() {
        //Given
        List<TrelloList> trelloList = List.of(new TrelloList("1", "Test", false));
        List<TrelloBoard> trelloBoard = List.of(new TrelloBoard("Test1", "1", trelloList));

        //When
        List<TrelloBoardDto> trelloBoardsDtos = trelloMapper.mapToBoardsDto(trelloBoard);

        //Then
        assertEquals("1", trelloBoardsDtos.get(0).getId());
        assertEquals("Test1", trelloBoardsDtos.get(0).getName());
        assertEquals(1, trelloList.size());

    }

    @Test
    public void testMapToCardDto() {
        //Given
        TrelloCard card = new TrelloCard("Test", "description", "1", "1");

        //When
        TrelloCardDto cardDto = trelloMapper.mapToCardDto(card);

        //Then
        assertEquals(card.getName(), cardDto.getName());
        assertEquals(card.getDescription(), cardDto.getDescription());
        assertEquals(card.getPos(), cardDto.getPos());
        assertEquals(card.getListId(), cardDto.getListId());
    }

    @Test
    public void testMapToCard() {
        //Given
        TrelloCardDto cardDto = new TrelloCardDto("Test", "description", "1", "1");

        //When
        TrelloCard card = trelloMapper.mapToCard(cardDto);

        //Then
        assertEquals(cardDto.getName(), card.getName());
        assertEquals(cardDto.getDescription(), card.getDescription());
        assertEquals(cardDto.getPos(), card.getPos());
        assertEquals(cardDto.getListId(), card.getListId());
    }
}
