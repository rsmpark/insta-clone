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
        "import/order": [
            "warn",
            {
                alphabetize: {
                    caseInsensitive: true,
                    order: "asc"
                },
                groups: [
                    "builtin",
                    "external",
                    "index",
                    "sibling",
                    "parent",
                    "internal"
                ]
            }
        ],
        semi: [2, "always"],
        "object-curly-spacing": ["error", "always"],
        "react/prop-types": "off"
    }
};
