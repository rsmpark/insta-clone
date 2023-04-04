import React, {Component} from "react";
import "./App.css";
import {Route, withRouter, Switch} from "react-router-dom";
import {Layout} from "antd";
import Login from "../login/Login";

class App extends Component {
    state = {
        currentUser: null,
        isAuthenticated: false,
        isLoading: false
    };

    render() {
        return (
            <Layout className="layout">
                <div className="container">
                    <Switch>
                        <Route
                            exact
                            path="/login"
                            render={props => (
                                <Login
                                    isAuthenticated={this.state.isAuthenticated}
                                    currentUser={this.state.currentUser}
                                    {...props}
                                />
                            )}
                        />
                    </Switch>
                </div>
            </Layout>
        );
    }
}

export default withRouter(App);