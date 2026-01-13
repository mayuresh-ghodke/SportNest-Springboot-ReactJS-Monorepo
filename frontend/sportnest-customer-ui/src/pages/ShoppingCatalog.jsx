import { useState, useCallback } from "react";
import CategoriesAndSubCategories from "../components/CategoriesAndSubCategories";
import ProductList from "./ProductList";

const ShoppingCatalog = () => {
  const [selectedCategoryId, setSelectedCategoryId] = useState(null);
  const [selectedSubCategoryId, setSelectedSubCategoryId] = useState(null);

  const handleCategorySelect = useCallback((categoryId) => {
    setSelectedCategoryId(categoryId);
    setSelectedSubCategoryId(null); 
  }, []);

  const handleSubCategorySelect = useCallback((sub) => {
    setSelectedSubCategoryId(sub.id);
  }, []);

  return (
    <div className="container-fluid p-5">
      <div className="row">
        <aside className="col-md-2 border-end">
          <CategoriesAndSubCategories
            onCategorySelect={handleCategorySelect}
            onSubCategorySelect={handleSubCategorySelect}
          />
        </aside>

        <main className="col-md-10">
          <ProductList
            categoryId={selectedCategoryId}
            subCategoryId={selectedSubCategoryId}
          />
        </main>
      </div>
    </div>
  );
};

export default ShoppingCatalog;
