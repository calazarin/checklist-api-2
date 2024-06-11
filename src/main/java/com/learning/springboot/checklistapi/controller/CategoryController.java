package com.learning.springboot.checklistapi.controller;

import com.learning.springboot.checklistapi.dto.CategoryDTO;
import com.learning.springboot.checklistapi.dto.NewResourceDTO;
import com.learning.springboot.checklistapi.entity.CategoryEntity;
import com.learning.springboot.checklistapi.exception.ExceptionalResponse;
import com.learning.springboot.checklistapi.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.ValidationException;
import java.util.List;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/v1/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService){
        this.categoryService = categoryService;
    }

    @Operation(description = "Retrieves all categories")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category list",
                    content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = CategoryDTO.class)))
            )
    })
    @GetMapping(value="", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CategoryDTO>> getAllCategories(){

        List<CategoryDTO> resp = StreamSupport.stream(this.categoryService
        .findAllCategories().spliterator(), false)
                .map(CategoryDTO::toDTO)
                .collect(toList());

        return new ResponseEntity<>(resp, HttpStatus.OK);
    }

    @Operation(description = "Inserts a new category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created new category",
                    content = @Content(schema = @Schema(anyOf = {NewResourceDTO.class}))),
            @ApiResponse(responseCode = "422", description = "Provided category name cannot be empty or null",
                    content = @Content(schema = @Schema(anyOf = {ExceptionalResponse.class})))
    })
    @PostMapping(value ="", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<NewResourceDTO> addNewCategory(@RequestBody CategoryDTO categoryDTO){

        CategoryEntity newCategory = this.categoryService.addNewCategory(categoryDTO.getName());

        return new ResponseEntity<>(new NewResourceDTO(newCategory.getGuid()), HttpStatus.CREATED);
    }

    @Operation(description = "Updates a category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Category updated"),
            @ApiResponse(responseCode = "422", description = "Provided category guid cannot be empty or null",
                    content = @Content(schema = @Schema(anyOf = {ExceptionalResponse.class}))),
            @ApiResponse(responseCode = "404", description = "Provided category guid does not exist",
                    content = @Content(schema = @Schema(anyOf = {ExceptionalResponse.class})))
    })
    @PutMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateCategory(@RequestBody CategoryDTO categoryDTO) {

        if(!StringUtils.hasText(categoryDTO.getGuid())){
            throw new ValidationException("Category cannot be null or empty");
        }

        this.categoryService.updateCategory(categoryDTO.getGuid(), categoryDTO.getName());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(description = "Deletes a category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Category deleted"),
            @ApiResponse(responseCode = "422", description = "Provided category guid cannot be empty or null",
                    content = @Content(schema = @Schema(anyOf = {ExceptionalResponse.class}))),
            @ApiResponse(responseCode = "404", description = "Provided category guid does not exist",
                    content = @Content(schema = @Schema(anyOf = {ExceptionalResponse.class}))),
            @ApiResponse(responseCode = "500", description = "Category cannot be deleted",
                    content = @Content(schema = @Schema(anyOf = {ExceptionalResponse.class}))),
    })
    @DeleteMapping(value = "{guid}")
    public ResponseEntity<Void> deleteCategory(@PathVariable String guid){
        this.categoryService.deleteCategory(guid);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
