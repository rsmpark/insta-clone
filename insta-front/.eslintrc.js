module.exports = {
    env: {
        browser: true,
        es2021: true
    },
    extends: [
        "standard",
        "eslint:recommended",
        "plugin:jest/recommended",
        "plugin:jsx-a11y/recommended",
        "plugin:react/recommended",
        "plugin:react-hooks/recommended",
        "plugin:testing-library/react"
    ],
    overrides: [],
    parserOptions: {
        ecmaVersion: "latest",
        sourceType: "module"
    },
    plugins: [
        "react"
    ],
    rules: {
        indent: [
            "error",
            4
        ],
        quotes: [
            "warn",
            "double"
        ],
        "react/no-unescaped-entities": 0,
        "import/order": [
            "error",
            {
                groups: ["builtin", "external", "internal", ["parent", "sibling"]],
                pathGroups: [
                    {
                        pattern: "react",
                        group: "external",
                        position: "before"
                    },
                    {
                        pattern: "react-dom",
                        group: "external",
                        position: "before"
                    }
                ],
                pathGroupsExcludedImportTypes: ["react"],
                "newlines-between": "always",
                alphabetize: {
                    order: "asc",
                    caseInsensitive: true
                }
            }
        ],
        semi: [2, "always"],
        "object-curly-spacing": ["error", "always"],
        "react/prop-types": "off"
    }
};
