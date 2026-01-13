import { useEffect, useState } from "react";
import ListGroup from "react-bootstrap/ListGroup";
import { allCategories } from "../services/categoryService";
import { getSubCategoriesByCategoryId } from "../services/subCategoryService";

const CategoriesAndSubCategories = ({ onCategorySelect, onSubCategorySelect }) => {

  const [categories, setCategories] = useState([]);
  const [subCategories, setSubCategories] = useState([]);
  const [activeCategoryId, setActiveCategoryId] = useState(null);
  const [subCategoryCache, setSubCategoryCache] = useState({});
  const [loadingSub, setLoadingSub] = useState(false);

  useEffect(() => {
    const fetchCategories = async () => {
      const data = await allCategories();
      setCategories(Array.isArray(data) ? data : []);
    };
    fetchCategories();
  }, []);

  const handleCategoryClick = async (categoryId) => {
    setActiveCategoryId(categoryId);
    onCategorySelect?.(categoryId);

    if (subCategoryCache[categoryId]) {
      setSubCategories(subCategoryCache[categoryId]);
      return;
    }

    setLoadingSub(true);
    const subs = await getSubCategoriesByCategoryId(categoryId);
    const safeSubs = Array.isArray(subs) ? subs : [];

    setSubCategories(safeSubs);
    setSubCategoryCache(prev => ({
      ...prev,
      [categoryId]: safeSubs
    }));
    setLoadingSub(false);
  };

  return (
    <div className="catalog-sidebar">
      <h5 className="mb-3">Categories</h5>

      <ListGroup variant="flush">
        {categories.map(cat => (
          <div key={cat.id}>
            <ListGroup.Item
              action
              active={activeCategoryId === cat.id}
              onClick={() => handleCategoryClick(cat.id)}
              className="fw-semibold"
            >
              {cat.categoryName}
            </ListGroup.Item>

            {activeCategoryId === cat.id && (
              <ListGroup className="ms-3 mt-2 mb-2">

                {loadingSub && (
                  <div className="text-muted ms-3 mb-2">
                    Loading subcategories...
                  </div>
                )}

                {!loadingSub && subCategories.length > 0 && (
                  subCategories.map(sub => (
                    <ListGroup.Item
                      key={sub.id}
                      action
                      onClick={(e) => {
                        e.stopPropagation();
                        onSubCategorySelect?.(sub);
                      }}
                      className="sub-category-item"
                    >
                      {sub.subCategoryName}
                    </ListGroup.Item>
                  ))
                )}

                {!loadingSub && subCategories.length === 0 && (
                  <div className="text-muted ms-3 mb-2">
                    No subcategories
                  </div>
                )}
              </ListGroup>
            )}
          </div>
        ))}
      </ListGroup>
    </div>
  );
};

export default CategoriesAndSubCategories;
